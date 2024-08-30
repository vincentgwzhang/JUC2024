package org.example.juc.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class LSellAndProduceResource {

    private int resourceCount = 0;
    private int sellCount = 0;
    private int produceCount = 0;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition sellerCondition = lock.newCondition();
    private final Condition producerCondition = lock.newCondition();

    public void sell() throws InterruptedException {
        lock.lock();

        try {
            while (resourceCount == 0) {
                sellerCondition.await();
            }
            resourceCount--;
            sellCount++;
            log.info("sell resource count:{}", sellCount);
            producerCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void produce() throws InterruptedException {
        lock.lock();

        try {
            while (resourceCount != 0) {
                producerCondition.await();
            }
            resourceCount++;
            produceCount++;
            log.info("produce resource count:{}", produceCount);
            sellerCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

}

public class LSellAndProduce {

    public static void main(String[] args) {

        LSellAndProduceResource resource = new LSellAndProduceResource();

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
        private final LSellAndProduceResource resource;
        private final Role role;

        public Worker(Role role, LSellAndProduceResource resource, int times) {
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
