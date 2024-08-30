package org.example.juc.sync;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class Ticket {

    private int number = 30;

    public synchronized void sale() {

        if(number > 0) {
            log.info("Thread name: [{}], Original ticket count: [{}], now ticket count: [{}]", Thread.currentThread().getName(), number, --number);
        }
    }
}

@Slf4j
public class TicketSeller {

    public static void main(String[] args) throws InterruptedException {
        Ticket ticket = new Ticket();
        for (int index = 1; index <= 3; index++) {
            new Saller(ticket).start();
        }

        while (Thread.activeCount() != 2) {
            Thread.yield();
        }

        log.info("Thread name: [{}] run finish", Thread.currentThread().getName());
    }

    private static class Saller extends Thread {

        private Ticket ticket;

        public Saller(Ticket ticket) {
            this.ticket = ticket;
        }

        @Override
        public void run() {
            for (int i = 0; i < 40; i++) {
                ticket.sale();
                try {
                    TimeUnit.MILLISECONDS.sleep(new Random().nextInt(100) + 50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
