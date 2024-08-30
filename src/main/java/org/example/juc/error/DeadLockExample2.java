package org.example.juc.error;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeadLockExample2 {

    @Getter
    static class BankAccount {
        private final String accountName;
        private final Lock lock = new ReentrantLock();

        public BankAccount(String accountName) {
            this.accountName = accountName;
        }

        public void transfer(BankAccount account) {
            this.getLock().lock();
            try {
                log.info("Thread [{}] locked account [{}]", Thread.currentThread().getName(), this.getAccountName());
                Thread.sleep(100);
                account.getLock().lock();
                account.getLock().unlock();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                this.getLock().unlock();
            }
        }
    }

    public static void main(String[] args) {
        BankAccount accountA = new BankAccount("AccountA");
        BankAccount accountB = new BankAccount("AccountB");

        Thread thread1 = new Thread(() -> accountA.transfer(accountB), "Thread-1");
        Thread thread2 = new Thread(() -> accountB.transfer(accountA), "Thread-2");

        thread1.start();
        thread2.start();

        while (Thread.activeCount() != 2) {
            Thread.yield();
        }
    }
}
