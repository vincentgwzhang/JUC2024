package org.example.juc.sync;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class SellAndProduceResource {

    private int resourceCount = 0;
    private int sellCount = 0;
    private int produceCount = 0;

    public synchronized void sell() throws InterruptedException {
        while (resourceCount == 0) {
            wait();
        }

        resourceCount--;
        sellCount++;
        log.info("sell resource count:{}", sellCount);
        notifyAll();
    }

    public synchronized void produce() throws InterruptedException {
        while (resourceCount != 0) {
            wait();
        }

        resourceCount++;
        produceCount++;
        log.info("produce resource count:{}", produceCount);
        notifyAll();
    }

}

public class SellAndProduce {

    public static void main(String[] args) {

        SellAndProduceResource resource = new SellAndProduceResource();

        int[] producer = new int[] {51, 62, 93};
        int[] consumer = new int[] {47, 20, 30, 56, 3, 25, 25};

        for (int times : producer) {
            new Worker(Role.PRODUCER, resource, times).start();
        }

        for (int times : consumer) {
            new Worker(Role.CONSUMER, resource, times).start();
        }

        while (Thread.activeCount() != 2) {
            Thread.yield();
        }

    }

    private enum Role {
        PRODUCER,
        CONSUMER
    }

    private static class Worker extends Thread {
        private final int times;
        private final SellAndProduceResource resource;
        private final Role role;

        public Worker(Role role, SellAndProduceResource resource, int times) {
            this.role = role;
            this.resource = resource;
            this.times = times;
        }

        @Override
        public void run() {
            for (int index = 0; index < times; index++) {
                try {
                    if (role == Role.PRODUCER) {
                        resource.produce();
                    } else {
                        resource.sell();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
