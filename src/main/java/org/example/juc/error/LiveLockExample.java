package org.example.juc.error;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LiveLockExample {

    static class Spoon {
        private Lock lock = new ReentrantLock();
        private boolean isTaken = false;

        public boolean tryToTake() {
            return lock.tryLock();
        }

        public void putDown() {
            lock.unlock();
        }

        public boolean isTaken() {
            return isTaken;
        }

        public void take() {
            isTaken = true;
        }

        public void release() {
            isTaken = false;
        }
    }

    static class Person {
        private final String name;
        private boolean isHungry;

        public Person(String name) {
            this.name = name;
            this.isHungry = true;
        }

        public void eatWith(Spoon spoon, Person spouse) {
            while (isHungry) {
                if (spoon.tryToTake()) {
                    System.out.println(name + " took the spoon.");
                    if (spouse.isHungry) {
                        System.out.println(name + " sees that " + spouse.name + " is hungry, so " + name + " puts the spoon down.");
                        spoon.putDown();  // 放下勺子以便配偶可以用
                    } else {
                        spoon.take();
                        System.out.println(name + " is eating now.");
                        isHungry = false;
                        spoon.release();
                        spoon.putDown();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Spoon sharedSpoon = new Spoon();
        Person husband = new Person("Husband");
        Person wife = new Person("Wife");

        new Thread(() -> husband.eatWith(sharedSpoon, wife)).start();
        new Thread(() -> wife.eatWith(sharedSpoon, husband)).start();
    }
}
