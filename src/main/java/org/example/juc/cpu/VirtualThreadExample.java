package org.example.juc.cpu;

import java.util.concurrent.TimeUnit;

public class VirtualThreadExample {

    public static void main(String[] args) throws InterruptedException {
        int parallelism = Runtime.getRuntime().availableProcessors();

        for (int i = 0; i < parallelism; i++) {
            Thread.ofVirtual().start(() -> {
                System.out.println("Running on virtual thread: " + Thread.currentThread());
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        int m = 1;
        while (m-- > 0) {
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
