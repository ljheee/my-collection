package com.ljheee.collection.queue;

import java.util.ArrayList;
import java.util.List;

/**
 * 线程安全 阻塞队列
 */
public class MyBlockingQueue<T> {

    // 存储队列元素 FIFO
    private List<T> list = new ArrayList();

    // 自定义对象锁
    Object lock = new Object();

    private int size;

    public MyBlockingQueue(int size) {
        this.size = size;
    }

    public MyBlockingQueue() {
        this(10);
    }

    public void put(T item){

        try {
            synchronized (lock){
                if(list.size() == size){
                    lock.wait();//池满 等待，同时释放lock
                }
                list.add(item);
                lock.notifyAll();// 唤醒池中等待的线程
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    public T get(){
        T t = null;
        try {
            synchronized (lock){
                if(list.size() == 0){
                    lock.wait();
                }
                t= list.get(0);
                list.remove(0);
                lock.notifyAll();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return t;
    }


    public static void main(String[] args) {
        MyBlockingQueue<String> queue = new MyBlockingQueue<>(3);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 20; i++) {
                    queue.put("a"+i);
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(queue.get());
                System.out.println(queue.get());
                System.out.println(queue.get());
                System.out.println(queue.get());
                System.out.println(queue.get());
            }
        }).start();
    }

}
