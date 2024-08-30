package org.example.juc.callable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class CallableImpl implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        log.info("{} come in callable", Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(5);
        return 200;
    }
}

@Slf4j
public class CallableStudy {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        FutureTask<Integer> futureTask1 = new FutureTask<>(new CallableImpl());
        new Thread(futureTask1).start();

        while(!futureTask1.isDone()) {
            Thread.yield();
        }

        log.info("{}", futureTask1.get());

        /**
         *
         *
         * 注意了, 其实这个做法是错的!!!!!!
         *
         * 绝对不能复用同一个 instance, 否则 FutureTask 会在第一次执行后直接缓存结果的
         *
         */
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(futureTask1);
        log.info("{}", futureTask1.get());
        executor.shutdownNow();
    }

}
