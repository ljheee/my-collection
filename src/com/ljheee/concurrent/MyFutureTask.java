package com.ljheee.concurrent;

import java.util.concurrent.*;

/**
 * FutureTask传入一个Callable，FutureTask.get()方法会阻塞等待callable接口返回值。
 */
public class MyFutureTask<V> implements Runnable, Future<V> {

    private Callable<V> callable;
    private V result = null;


    public MyFutureTask(Callable<V> callable) {
        this.callable = callable;
    }

    @Override
    public void run() {
        try {
            result = callable.call();

            synchronized (this) {//如果执行到这行，说明结果已返回
                this.notify();//唤醒等待结果的get()方法
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return result != null;
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {

        if (result != null) {
            return result;
        }

        synchronized (this) {
            this.wait();
        }

        return result;
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (result != null) {
            return result;
        }

        if (timeout > 0L) {
            unit.sleep(timeout);
            if (result != null) {
                return result;
            } else {
                throw new TimeoutException();
            }
        }
        return result;
    }
}
