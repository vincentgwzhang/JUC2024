package org.example.juc.completable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompletableFutureDemo {
    public static void main(String[] args) throws Exception {

        CompletableFuture<Void> completableFuture1 = CompletableFuture.runAsync(()->{
            log.info("{} : CompletableFuture1", Thread.currentThread().getName());
        });
        completableFuture1.get();

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(()->{
            log.info("{} : CompletableFuture2", Thread.currentThread().getName());
            //模拟异常
            int i = 10/0;
            return 1024;
        });

        completableFuture2.whenComplete((t,u)->{
            System.out.println("------t="+t); // t 是结果
            System.out.println("------u="+u.getCause().getClass().getName()); // u 代表exception
        }).get();

    }
}
