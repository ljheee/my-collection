

CountDownLatch
场景1：模拟并发，并发线程一起执行
CountDownLatch(1)
所有线程启动，线程方法体第一行都调用CountDownLatch.await()，让所有线程启动后等待；
然后在主线程 调用CountDownLatch.countdown(1)，让所有线程一起往下。

场景2：多个线程(任务)完成后，进行汇总合并
CountDownLatch(n)
在每个线程(任务) 完成的最后一行加上CountDownLatch.countDown()，让计数器-1；
所有线程完成-1，计数器减到0后，主线程继续执行汇总任务。
```
    public static void main(String[] args) throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            final int index = i;
            new Thread(() -> {
                try {
                    Thread.sleep(1000 + ThreadLocalRandom.current().nextInt(1000));
                    System.out.println("finish" + index + Thread.currentThread().getName());
                    countDownLatch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        countDownLatch.await();
        System.out.println("主线程:在所有任务运行完成后，就行汇总");
    }
```
