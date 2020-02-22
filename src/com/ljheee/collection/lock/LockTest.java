package com.ljheee.collection.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ljheee on 2019/2/26.
 */
public class LockTest {


    int i = 0;
//    Lock lock = new ReentrantLock();
//    Lock lock = new MyLock();
    Lock lock = new MyReentrantLock();


    public void add() {
        lock.lock();
        try {
            i++;
        } finally {
            lock.unlock();
        }
    }


    public static void main(String[] args) throws InterruptedException {

        LockTest lockTest = new LockTest();

        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    lockTest.add();
                }
            }).start();
        }
        Thread.sleep(2500);
        System.out.println(lockTest.i);//200
    }
}
