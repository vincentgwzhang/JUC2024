package org.example.juc.cpu;

import java.util.concurrent.TimeUnit;

public class DiffCPU {

    public native void bindThreadToCpu(int cpuId);

    static {
        System.loadLibrary("ThreadAffinityLib");
    }

    public static void main(String[] args) {
        DiffCPU affinity = new DiffCPU();

        Thread thread1 = new Thread(() -> {
            affinity.bindThreadToCpu(0);
            int m = 10;
            while (m -- >= 0) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("Thread 1 running on CPU 0");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            affinity.bindThreadToCpu(1);
            int m = 10;
            while (m -- >= 0) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("Thread 1 running on CPU 0");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        thread1.start();
        thread2.start();

        while(Thread.activeCount() > 2) {
            Thread.yield();
        }
    }

}
