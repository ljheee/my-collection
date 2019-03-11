package com.ljheee.concurrent.poll;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 固定数量的工作线程
 */
public class MyFixedThreadPool {
    //存放任务的阻塞队列
    private BlockingQueue<Runnable> taskQueue;
    private List<Worker> workers;

    private volatile boolean working = true;// 是否关闭线程池


    public MyFixedThreadPool(int taskSize, int workSize) {
        if (taskSize < 0 || workSize < 0) {
            throw new IllegalArgumentException("IllegalArgumentException");
        }
        taskQueue = new LinkedBlockingQueue<>(taskSize);
        workers = Collections.synchronizedList(new ArrayList<Worker>(workSize));

        // 初始化固定数量的工作线程
        for (int i = 0; i < workSize; i++) {
            Worker w = new Worker(this);
            workers.add(w);
            w.start();
        }
    }

    //提交任务的方法
    //如果仓库满了就返回false
    public boolean submit(Runnable task) {
        if (working) {
            return taskQueue.offer(task);
        }
        return false;
    }

    public void shutDown() {
        working = false;
        for (Worker w : workers) {
            // 这些状态的线程都是在shutDown()之前 就进行blockingQueue.take()阻塞take的线程
            if (w.getState().equals(Thread.State.BLOCKED)
                    || w.getState().equals(Thread.State.WAITING)
                    || w.getState().equals(Thread.State.TIMED_WAITING)) {
                w.interrupt();
//                w.notify();
            }
        }
    }


    class Worker extends Thread {
        MyFixedThreadPool pool;

        public Worker(MyFixedThreadPool pool) {
            this.pool = pool;
        }

        @Override
        public void run() {
            int count = 0;

            while (pool.working) {
                Runnable task = null;
                try {

                    if (pool.working) {
                        task = pool.taskQueue.take();//取不到就阻塞，底层会LockSuport.park当前线程，使其阻塞WAITING
                    } else {
                        task = pool.taskQueue.poll();// 取不到，返回特殊值null
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (task != null) {
                    task.run();
                }
                System.out.println(Thread.currentThread() + "线程运行了" + (++count));
            }

        }
    }


    public static void main(String[] args) throws InterruptedException {

        MyFixedThreadPool pool = new MyFixedThreadPool(6, 3);
        for (int i = 0; i < 20; i++) {
            boolean success = pool.submit(() -> {
                System.out.println("执行任务");
            });
            System.out.println("submit result=" + success);
        }
//        Thread.sleep(2000);
//        System.out.println(pool.taskQueue.poll());//待上面执行完成，队列为空，poll()出的为null

//        pool.shutDown();

    }
}
