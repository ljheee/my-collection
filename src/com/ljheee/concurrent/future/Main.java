package com.ljheee.concurrent.future;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * 测试类
 *
 */
public class Main {


    public static void main(String[] args) throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(5);


        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(5000);
                return "abc";
            }
        };

        MyFutureTask<String> future = new MyFutureTask<>(callable);
        executor.submit(future);


        future.addListener(new FutureListener<Future<? super String>>() {
            @Override
            public void onComplete(Future future) throws Exception {
                System.out.println("结果完成");
                Object o = future.get();
                System.out.println(o);
            }
        });

        System.out.println(future.get());

    }
}
