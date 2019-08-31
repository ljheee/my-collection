package com.ljheee.concurrent.lock;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.LockSupport;

/**
 * 模仿AQS
 * 自旋+队列实现锁
 */
public class MyLock {

    protected volatile int state = 0;
    volatile Thread owner = null;


    public int getState() {
        return state;
    }

    public Thread getOwner() {
        return owner;
    }

    public void setOwner(Thread owner) {
        this.owner = owner;
    }

    ConcurrentLinkedDeque<Thread> queue = new ConcurrentLinkedDeque<>();


    public boolean acquire() {
        Thread current = Thread.currentThread();
        int c = getState();
        if (c == 0) {
            if ((queue.size() == 0 || queue.peek() == current) && compareAndSwap(0, 1)) {// 不加queue.peek() == current只有第一个线程能进
                setOwner(current);
                return true;
            }
        }
        return false;
    }


    public void lock() {
        if (acquire()) {
            return;
        }
        Thread current = Thread.currentThread();
        queue.add(current);

        for (; ; ) {
            if (queue.peek() == current && acquire()) {
                queue.poll();// 被唤醒
                return;
            }
            LockSupport.park();
        }
    }

    public void unlock() {
        Thread current = Thread.currentThread();

        if (current != getOwner()) {
            throw new IllegalStateException("IllegalStateException");
        }

        int c = getState();
        if (c > 0 && compareAndSwap(1, 0)) {
            setOwner(null);
            Thread thread = queue.peek();//唤醒队头
            if (thread != null) {
                LockSupport.unpark(thread);
            }
        }
    }

    //=================================================================

    private static Unsafe unsafe;
    private static long stateOffset;

    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            unsafe = (Unsafe) theUnsafe.get(Unsafe.class);
            stateOffset = unsafe.objectFieldOffset(MyLock.class.getDeclaredField("state"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private boolean compareAndSwap(int expected, int update) {
        return unsafe.compareAndSwapInt(this, stateOffset, expected, update);
    }


    public static void main(String[] args) {

        CountDownLatch countDownLatch = new CountDownLatch(1);

        MyLock lock = new MyLock();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    countDownLatch.await();
                    lock.lock();
                    Thread.sleep(1000 + ThreadLocalRandom.current().nextInt(1000));
                    System.out.println("okkkkkkkkkk==" + Thread.currentThread().getName());
                    lock.unlock();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        countDownLatch.countDown();
    }


}
