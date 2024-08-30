package org.example.juc.error;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeadLockExample {

    @Getter
    static class BankAccount {
        private final String accountName;
        private final Lock lock = new ReentrantLock();

        public BankAccount(String accountName) {
            this.accountName = accountName;
        }
    }

    public static void transfer(BankAccount fromAccount, BankAccount toAccount) {
        fromAccount.getLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " locked " + fromAccount.getAccountName());
            Thread.sleep(100);  // 模拟一些处理时间
            toAccount.getLock().lock();
            toAccount.getLock().unlock();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            fromAccount.getLock().unlock();
        }
    }

    public static void main(String[] args) {
        BankAccount accountA = new BankAccount("AccountA");
        BankAccount accountB = new BankAccount("AccountB");

        Thread thread1 = new Thread(() -> transfer(accountA, accountB), "Thread-1");
        Thread thread2 = new Thread(() -> transfer(accountB, accountA), "Thread-2");

        thread1.start();
        thread2.start();
    }
}
