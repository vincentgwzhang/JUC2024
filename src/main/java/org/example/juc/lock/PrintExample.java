package org.example.juc.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrintExample {

    enum Turn {
        A, B, C
    }

    static class Print {
        private final ReentrantLock lock = new ReentrantLock();
        private final Condition conditionAA = lock.newCondition();
        private final Condition conditionBB = lock.newCondition();
        private final Condition conditionCC = lock.newCondition();
        private Turn turn = Turn.A;

        public void printA() {
            lock.lock();
            try {
                while (turn != Turn.A) {
                    conditionAA.await();
                }

                log.info("Hello I am {}", Thread.currentThread().getName());

                this.turn = Turn.B;
                conditionBB.signal();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                lock.unlock();
            }
        }

        public void printB() {
            lock.lock();
            try {
                while (turn != Turn.B) {
                    conditionBB.await();
                }

                log.info("Hello I am {}", Thread.currentThread().getName());

                this.turn = Turn.C;
                conditionCC.signal();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                lock.unlock();
            }
        }

        public void printC() {
            lock.lock();
            try {
                while (turn != Turn.C) {
                    conditionCC.await();
                }

                log.info("Hello I am {}", Thread.currentThread().getName());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        Print print = new Print();

        new Thread(print::printC, "Thread-C").start();
        new Thread(print::printA, "Thread-A").start();
        new Thread(print::printB, "Thread-B").start();

        while (Thread.activeCount() != 2) {
            Thread.yield();
        }
    }

}
