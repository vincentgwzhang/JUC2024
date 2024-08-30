package org.example.juc.queue;

import java.util.concurrent.LinkedTransferQueue;

public class LinkedTransferQueueExample {

    public static void main(String[] args) {
        LinkedTransferQueue<String> queue = new LinkedTransferQueue<>();

        // 生产者线程
        Thread producer = new Thread(() -> {
            try {
                String[] messages = {"Message 1", "Message 2", "Message 3"};
                for (String message : messages) {
                    System.out.println("Producing: " + message);
                    queue.transfer(message); // 使用 transfer() 方法直接传递消息
                    System.out.println("Message transferred: " + message);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // 消费者线程
        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    String message = queue.take(); // 从队列中取出消息
                    System.out.println("Consuming: " + message);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // 启动消费者线程
        consumer.start();

        // 延迟启动生产者线程，模拟生产者等待消费者的场景
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        producer.start();
    }
}
