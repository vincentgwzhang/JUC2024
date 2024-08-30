package org.example.juc.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import lombok.extern.slf4j.Slf4j;

class WorkerTask extends RecursiveTask<Integer> {
    private static final int THRESHOLD = 100;
    private int begin ;
    private int end;
    private int result ;

    public WorkerTask(int begin,int end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if (end - begin < THRESHOLD) {
            for (int i = begin; i <= end; i++) {
                result += i;
            }
        } else {
            int middle = (begin + end) / 2;
            WorkerTask left = new WorkerTask(begin, middle);
            WorkerTask right = new WorkerTask(middle+1, end);
            left.fork();
            right.fork();
            result = left.join() + right.join();
        }
        return result;
    }
}

@Slf4j
public class ForkJoinDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        WorkerTask myTask = new WorkerTask(0,100);
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        ForkJoinTask<Integer> forkJoinTask = forkJoinPool.submit(myTask);
        Integer result = forkJoinTask.get();
        log.info("result: {}", result);
        forkJoinPool.shutdown();
    }
}
