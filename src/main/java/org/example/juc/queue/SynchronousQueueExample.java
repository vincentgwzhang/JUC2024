package org.example.juc.queue;

import java.util.concurrent.SynchronousQueue;

public class SynchronousQueueExample {

    public static void main(String[] args) {
        // 创建一个 SynchronousQueue 队列
        SynchronousQueue<String> queue = new SynchronousQueue<>();

        // 生产者线程
        Thread producer = new Thread(() -> {
            try {
                String[] messages = {"Message 1", "Message 2", "Message 3"};
                for (String message : messages) {
                    queue.put(message);
                    System.out.println("Producing: " + message);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // 消费者线程
        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    String message = queue.take();
                    System.out.println("Consuming: " + message);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // 启动生产者和消费者线程
        producer.start();
        consumer.start();
    }
}
