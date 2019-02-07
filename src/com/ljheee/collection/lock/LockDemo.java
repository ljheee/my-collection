package com.ljheee.collection.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by lijianhua04 on 2018/12/27.
 */
public class LockDemo {


    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();

        String a = "a";
        try {
            System.out.println(a);
            lock.lockInterruptibly();
            System.out.println(a);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
