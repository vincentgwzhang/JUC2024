package org.example.juc.lock;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class LTicket {

    private int number = 30;

    private int logCount = 0;

    private ReentrantLock lock = new ReentrantLock();

    public boolean sale() {
        lock.lock();
        try {
            if(number > 0) {
                logCount++;
                log.info("LogCount = {}. Thread name: [{}], Original ticket count: [{}], now ticket count: [{}]", logCount, Thread.currentThread().getName(), number, --number);
                return true;
            }
        } finally {
            lock.unlock();
        }
        return false;
    }
}

@Slf4j
public class LTicketSeller {

    public static void main(String[] args) throws InterruptedException {
        LTicket ticket = new LTicket();
        for (int index = 1; index <= 3; index++) {
            new LTicketSeller.Saller(ticket).start();
        }

        while (Thread.activeCount() != 2) {
            Thread.yield();
        }

        log.info("Thread name: [{}] run finish", Thread.currentThread().getName());
    }

    private static class Saller extends Thread {

        private LTicket ticket;

        public Saller(LTicket ticket) {
            this.ticket = ticket;
        }

        @Override
        public void run() {
            for (int i = 0; i < 40; i++) {
                if (!ticket.sale()) {
                    break;
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(new Random().nextInt(100) + 50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
