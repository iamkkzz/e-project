package com.kkzz.mall.search;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class ThreadTest {
    public static ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.println("main start");
        long l = System.currentTimeMillis();
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("查询图片信息");
            return "jpg";
        }, executor);
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("查询商品属性");
            return "黑色";
        }, executor);
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("查询商品介绍");
            return "华为";
        }, executor);
//        CompletableFuture<Void> allOf = CompletableFuture.allOf(future1, future2, future3);
//        allOf.join();
        future3.join();future1.join();future2.join();
        System.out.println("总耗时为:" + (System.currentTimeMillis() - l));
        System.out.println("main end");
    }
}
