package org.example.juc.tools;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CountDownLatchDemo {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(6);

        for (int i = 0; i < 6; i++) {
            new Thread(() -> {
                try {
                    log.info("Thread {} execute finished", Thread.currentThread().getName());
                    TimeUnit.MILLISECONDS.sleep(new Random().nextInt(100) + 50);
                    countDownLatch.countDown();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }, "Thread-" + i).start();
        }

        countDownLatch.await();
        log.info("Thread {} execute finished, at the moment, Thread active count = {}", Thread.currentThread().getName(), Thread.activeCount());
    }

}
