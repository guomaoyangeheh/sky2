package com.gmy.sky2.future;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @Author guomaoyang
 * @Date 2021/4/21
 */
public class CompletableFutureTest {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Test
    public void allOfTest() throws ExecutionException, InterruptedException {
        List<CompletableFuture<Integer>> completableFutures = new ArrayList<>();
        // allOf()可以实现“所有CompletableFuture都必须成功”
        CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("cf1执行完毕");
            return 1;
        });
        CompletableFuture<Integer> cf2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("cf2执行完毕");
            return 2;
        });
        CompletableFuture<Integer> cf3 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("cf2执行完毕");
            return 10;
        });
        completableFutures.add(cf1);
        completableFutures.add(cf2);
        completableFutures.add(cf3);
        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]));
        CompletableFuture<List<Integer>> listCompletableFuture = completableFuture.thenApply(v -> {
            return completableFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        });
        List<Integer> list = listCompletableFuture.get();
        int sum = list.stream().mapToInt(Integer::intValue).sum();

        System.out.println(sum);
    }

    /**
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    void thenApplyTest() throws ExecutionException, InterruptedException {

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("thread-" + Thread.currentThread().getName());
            return "LiLei";
        }, executorService).thenApplyAsync(result -> {
            System.out.println("thread-" + Thread.currentThread().getName());
            return "Hello " + result;
        },executorService).thenApplyAsync(result -> {
            System.out.println("thread-" + Thread.currentThread().getName());
            return result + " Welcome To Home";
        },executorService);
        String s = completableFuture.get();
        System.out.println("result:"+s);
    }


    /**
     * 串行调用
     * @throws InterruptedException
     */
    @Test
    void test2() throws InterruptedException {
        CompletableFuture<String> completableFuture1 = CompletableFuture.supplyAsync(() -> queryCode("中国石油"));
        CompletableFuture<Double> completableFuture2 = completableFuture1.thenApplyAsync(CompletableFutureTest::fetchPrice);
        completableFuture2.thenAccept(result-> System.out.println("执行完毕，结果："+result));
        // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭:
        Thread.sleep(2000);
    }

    static String queryCode(String name) {
        System.out.println("queryCode执行时间："+ DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MS_PATTERN)+"-线程："+Thread.currentThread().getId());
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        return "601857";
    }

    static Double fetchPrice(String code) {
        System.out.println("fetchPrice执行时间："+ DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MS_PATTERN)+"-线程："+Thread.currentThread().getId());
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        return 5 + Math.random() * 20;
    }


    /**
     * 初体验
     * @throws InterruptedException
     */
    @Test
    void test1() throws InterruptedException {
        CompletableFuture<Double> completableFuture = CompletableFuture.supplyAsync(CompletableFutureTest::fetchPrice);
        completableFuture.thenAccept(result -> {
            System.out.println("运行成功，获得结果："+result);
        });
        completableFuture.exceptionally(e ->{
            System.out.println("发生异常，error："+e.getMessage());
            return null;
        });
        // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭:
        Thread.sleep(200);
    }
    private static Double fetchPrice() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        if (Math.random() < 0.8) {
            throw new RuntimeException("fetch price failed!");
        }
        return 5 + Math.random() * 20;
    }
}
