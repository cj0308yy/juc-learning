package org.pub.juc.base.completablefuture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class SplitParallel {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        List<CompletableFuture> futures = new ArrayList<>();
        CopyOnWriteArrayList<String> right = new CopyOnWriteArrayList<>();
        CopyOnWriteArrayList<String> error = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 31; i++) {
            int finalI = i;
            CompletableFuture<String> future = CompletableFuture.supplyAsync(()->{

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("time: "+finalI);
                    if(finalI % 2 == 0) {
                        int j = 10 / 0;
                    }

                return "No."+finalI;
            }, threadPool)
            .handle(new BiFunction<String, Throwable, String>() {
                @Override
                public String apply(String s, Throwable throwable) {
                    if(throwable != null) {
                        error.add(s);
                    } else {
                        right.add(s);
                    }
                    return null;
                }
            });

            futures.add(future);
        }


        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]))
                .thenRun(()->{
                    System.out.println("size:"+ right.size()+" :"+error.size());
                }).join();


        System.out.println("over");
        threadPool.shutdown();
    }
}
