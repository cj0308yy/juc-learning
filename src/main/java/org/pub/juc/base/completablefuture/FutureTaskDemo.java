package org.pub.juc.base.completablefuture;

import java.util.concurrent.*;

/**
 * FutureTask 实现异步、返回值 并行提高效率
 */
public class FutureTaskDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long l = System.currentTimeMillis();
//        serialWork();//耗时: 10016ms
        parallelWork(); //耗时: 5016ms

        long r = System.currentTimeMillis();
        System.out.println("耗时: "+(r-l) + "ms");
    }

    public static void serialWork() {
//        new Thread(()->{
            try {Thread.sleep(3000);} catch (InterruptedException e) { e.printStackTrace();}
            try {Thread.sleep(2000);} catch (InterruptedException e) { e.printStackTrace();}
            try {Thread.sleep(5000);} catch (InterruptedException e) { e.printStackTrace();}
//        }).start();

    }

    public static void parallelWork() throws ExecutionException, InterruptedException {
//        new Thread(()->{
        ThreadPoolExecutor executor = new ThreadPoolExecutor(8, 16,
                30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        FutureTask<String> task1 = new FutureTask<>(new Callable<String>() {

            @Override
            public String call() throws Exception {
                try {Thread.sleep(3000);} catch (InterruptedException e) { e.printStackTrace();}
                return null;
            }
        });
        executor.submit(task1);

        FutureTask<String> task2 = new FutureTask<>(new Callable<String>() {

            @Override
            public String call() throws Exception {
                try {Thread.sleep(2000);} catch (InterruptedException e) { e.printStackTrace();}
                return null;
            }
        });
        executor.submit(task2);
        FutureTask<String> task3 = new FutureTask<>(new Callable<String>() {

            @Override
            public String call() throws Exception {
                try {Thread.sleep(5000);} catch (InterruptedException e) { e.printStackTrace();}
                return null;
            }
        });
        executor.submit(task3);

        String s = task1.get();
        String s1 = task2.get();
        String s2 = task3.get();
    }
}
