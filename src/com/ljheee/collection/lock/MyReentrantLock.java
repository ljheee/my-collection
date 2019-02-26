package com.ljheee.collection.lock;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 *
 */
public class MyReentrantLock implements Lock {


    // 当前锁的 拥有者
    private AtomicReference<Thread> owner = new AtomicReference<>();

    //线程安全  等待队列
    private LinkedBlockingDeque<Thread> waiters = new LinkedBlockingDeque<>();

    @Override
    public boolean tryLock() {
        return owner.compareAndSet(null, Thread.currentThread());
    }

    /**
     * lock 相当于 强制拿到锁
     */
    @Override
    public void lock() {

        while(!owner.compareAndSet(null, Thread.currentThread())){
            waiters.offer(Thread.currentThread());
            LockSupport.park();
            waiters.remove(Thread.currentThread());//如果能够执行到这段的话，证明被唤醒了，所以要从等待列表中删除
        }

    }

    @Override
    public void unlock() {
        //CAS 修改owner 拥有者
        if (owner.compareAndSet(Thread.currentThread(), null)) {
            Iterator<Thread> iterator = waiters.iterator();
            while (iterator.hasNext()) {
                Thread next = iterator.next();
                LockSupport.unpark(next);

            }
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {

        long nanosTimeout = unit.toNanos(time);
        if (nanosTimeout <= 0L)
            return false;

        final long deadline = System.nanoTime() + nanosTimeout;
        if (!tryLock()) {
            nanosTimeout = deadline - System.nanoTime();
            if (nanosTimeout <= 0L)
                return false;
            if (Thread.interrupted())
                throw new InterruptedException();
            LockSupport.parkNanos(this, nanosTimeout);

        }
        return true;
    }


    @Override
    public Condition newCondition() {
        return null;
    }

}