package org.pub.juc.base.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

public class SimpleExec01 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        run();

//        consumer();

//        function();
//
//        compose();

        combine();
    }

    private static void combine() throws ExecutionException, InterruptedException {
        // 合并两个都执行完了的CompleteFuture
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);System.out.println("hello");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Hello";
        });
        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("world");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "World";
        });
        CompletableFuture<String> res = hello.thenCombine(world, new BiFunction<String, String, String>() {
            @Override
            public String apply(String s, String s2) {
                System.out.println("combine");
                return s + s2;
            }
        });
        String s = res.get();
        System.out.println(s);
    }

    private static void compose() throws InterruptedException, ExecutionException {
        // 给thenCompose传入返回CompletableFuture的supplier

        CompletableFuture<String> completableFuture
                = CompletableFuture.supplyAsync(() -> "Hello")
                .thenApply(s -> s + " Beautiful")
                .thenCompose(s -> CompletableFuture.supplyAsync(() -> s + " World"));
        System.out.println("completableFuture:"+completableFuture.get());
    }

    private static void function() throws InterruptedException, ExecutionException {
        // 注册Function任务
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<String> future = completableFuture.thenApply(s -> s + " World");
        System.out.println("thenApply:"+future.get()+" completableFuture:"+completableFuture.get());
    }

    private static void consumer() throws InterruptedException, ExecutionException {
        // 注册Consumer任务
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<Void> future = completableFuture.thenAccept(s -> System.out.println("Computation returned: " + s));
        future.get();// 输出Computation returned: Hello 返回null
        System.out.println("thenAccept: "+future.get()+"   completableFuture: "+completableFuture.get());
    }

    private static void run() throws InterruptedException, ExecutionException {
        // 注册Runnable任务
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<Void> future = completableFuture.thenRun(() -> System.out.println("Computation finished."));
        future.get();// 输出Computation finished. 返回null
        String s = completableFuture.get();
        System.out.println("thenRun: "+ future.get()+"   completableFuture: "+s);
    }
}
