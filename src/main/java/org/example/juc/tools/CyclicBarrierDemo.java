package org.example.juc.tools;

import java.util.concurrent.CyclicBarrier;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CyclicBarrierDemo {

    private static final int NUMBER = 7;

    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(NUMBER, ()-> log.info("All thread run finish, I action as last"));

        for (int i = 1; i <=7; i++) {
            new Thread(()->{
                try {
                    log.info("Thread {} run finish", Thread.currentThread().getName());
                    cyclicBarrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            },String.valueOf(i)).start();
        }
    }
}
