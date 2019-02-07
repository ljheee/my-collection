package com.ljheee.collection.lock;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * 自己实现锁
 * implements Lock
 */
public class MyLock implements Lock {


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

        boolean addQ = true;
        if (!tryLock()) {
            if (addQ) {
                // 添加到等待队列
                waiters.offer(Thread.currentThread());
                addQ = false;
            } else {
                // 挂起线程
                //后续，等待其他线程释放锁，收到通知之后继续循环
                LockSupport.park();
            }
        }
        waiters.remove(Thread.currentThread());
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


    //1）lock(), 拿不到lock就不罢休，不然线程就一直block。 比较无赖的做法。lock 强制拿到锁
    //2）tryLock()，马上返回，拿到lock就返回true，不然返回false。 比较潇洒的做法。
    //带时间限制的tryLock()，拿不到lock，就等一段时间，超时返回false。比较聪明的做法。
    //
    //3）lockInterruptibly()就稍微难理解一些。
}
