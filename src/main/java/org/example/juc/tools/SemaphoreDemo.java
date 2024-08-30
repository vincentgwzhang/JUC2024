package org.example.juc.tools;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
class ParkingPlace {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private int left = 30;

    public void park() {
        lock.writeLock().lock();
        left = left - 1;
        lock.writeLock().unlock();
    }

    public void left() {
        lock.writeLock().lock();
        left = left + 1;
        lock.writeLock().unlock();
    }

    public int getStatus() {
        lock.readLock().lock();
        int count = left;
        lock.readLock().unlock();
        return count;
    }

}

@Slf4j
public class SemaphoreDemo {

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3);
        ParkingPlace parkingPlace = new ParkingPlace();

        for (int i = 1; i <= 10; i++) {
            new Thread(new Car(parkingPlace, semaphore), "Thread-" + i).start();
        }

        new Thread(() -> {
            while (Thread.activeCount() != 3) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    log.info("There are {} seats left, while availablePermits = {}", parkingPlace.getStatus(), semaphore.availablePermits());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        while (Thread.activeCount() != 2) {
            Thread.yield();
        }
    }

    public static class Car extends Thread {

        private final ParkingPlace parkingPlace;
        private final Semaphore semaphore;

        public Car(ParkingPlace parkingPlace, Semaphore semaphore) {
            this.parkingPlace = parkingPlace;
            this.semaphore = semaphore;
        }

        public void run() {
            try {
                semaphore.acquire();
                parkingPlace.park();
                log.info("Thread {} get the seat", Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(new Random().nextInt(3) + 1);
                parkingPlace.left();
                log.info("Thread {} left", Thread.currentThread().getName());
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                semaphore.release();
            }
        }

    }

}
