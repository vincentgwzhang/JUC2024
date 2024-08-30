package org.example.juc.completable;

import java.util.concurrent.Callable;

public class CompletableFutureDemo2 {
}

class CompletableFutureDemo2Thread1 implements Runnable {

    @Override
    public void run() {

    }
}

class CompletableFutureDemo2Thread2 implements Callable<String> {

    @Override
    public String call() throws Exception {
        return null;
    }
}
