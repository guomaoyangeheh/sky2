package com.gmy.sky2.future;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @Author guomaoyang
 * @Date 2021/10/22
 */
public class CompletableFutureTest2 {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    /**
     *  创建一个CompletableFuture对象的几种方式
     *  可以通过下面4个静态方法进行创建CompletableFuture，那么他们有什么不同吗？
     *  runAsync()方法我们可以看到，需要传入一个Runnable类型的函数式接口，因此也就没有返回值
     *  supplyAsync()方法需要传入的是Supplier类型，因此在线程执行结束可以返回结果
     *  这2个方法都有一个重载方法，可以传入Executor对象。如果不传默认使用的是ForkJoinPool.commonPool() 作为它的线程池执行异步代码，
     *  也可以传入Executor对象指定线程池
     *
     *  public static CompletableFuture<Void> runAsync(Runnable runnable)
     *  public static CompletableFuture<Void> runAsync(Runnable runnable,Executor executor)
     *  public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier)
     *  public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier,Executor executor)
     */
    @Test
    public void runAsync(){
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            System.out.println("线程"+Thread.currentThread().getName()+"执行完毕...");
        });
        CompletableFuture<Void> completableFuture2 = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace(); }
            System.out.println("线程"+Thread.currentThread().getName()+"执行完毕...");
        },executorService);
        completableFuture.join();
        completableFuture2.join();
        System.out.println("主线程执行完毕...");
    }
    @Test
    public void supplyAsync(){
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(20);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            return 1 + 2;
        }, executorService);
        System.out.println("运行结果：" + completableFuture.join());
    }

    /**
     * 结果处理（不能对结果进行转换因为入参是BiConsumer函数式接口没有返回值）
     * 当CompletableFuture计算结果完成或者抛出异常时，我们可以执行特定的Action
     * 使用下面的方法我们可以在CompletableFuture计算完成之后执行指定的Action，whenComplete()的入参是个BiConsumer类型的接口函数，在方法中可以拿到2个参数
     * 分别是正常执行后的结果<T>和一个发生异常时的Throwable类型的对象。至于怎么使用就看开发者的意愿。
     * 带不带Async后缀的区别自安于 whenComplete()会直接使用相同的线程处理，whenCompleteAsync()则会使用其他的线程的处理
     * 至于whenCompleteAsync()两个重载方法的区别和supplyAsync()是一样的，不再赘述.
     *
     * 至于exceptionally()会在completableFuture异常完成时触发，入参是一个Function接口函数，会传入异常对象，针对异常处理后可以再返回一个结果。该方法返回是一个CompletableFuture对象
     * 如果执行完成返回的是原来的值，如果异常完成返回的是exceptionally()方法中指定的值。
     * 其实使用whenComplete()可以更灵活的处理异常。官方注释也是更推荐使用
     *
     * public CompletableFuture<T> whenComplete(BiConsumer<? super T,? super Throwable> action)
     * public CompletableFuture<T> whenCompleteAsync(BiConsumer<? super T,? super Throwable> action)
     * public CompletableFuture<T> whenCompleteAsync(BiConsumer<? super T,? super Throwable> action, Executor executor)
     * public CompletableFuture<T> exceptionally(Function<Throwable,? extends T> fn)
     *
     */
    @Test
    public void whenComplete(){
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            int i = new Random().nextInt();
            System.out.println(Thread.currentThread().getName());
            if (i % 2 == 0) {
                return 2;
            }else {
                int i1 = 1 / 0;
                return 1;
            }
        }, executorService);
        completableFuture.whenComplete((t,e)->{
           if(e != null){
               System.out.println(Thread.currentThread().getName()+"异常执行完成...errorMsg:" + e.getMessage());
           }else {
               System.out.println(Thread.currentThread().getName()+"结果："+t);
           }
        });
        /*CompletableFuture<Integer> exceptionally = completableFuture.exceptionally((e) -> {
            System.out.println("执行失败 错误信息：" + e.getMessage());
            System.out.println("虽然失败但是要返回默认值...");
            return 1;
        });*/
        System.out.println(completableFuture.join());
    }

    /**
     * 结果处理&转换
     * handle()的功能与whenComplete()类似，但是比他多了一个功能是可以进行"结果转换"
     * handle()的入参是个BiFunction类型的接口函数，因此有2个入参(param1,param2)+1个结果值(result)
     * 对接接口描述对这3个参数进行解释
     * param1: 原CompletableFuture返回的结果值
     * param2：Throwable 原CompletableFuture发生错误时的异常对象
     * result: handle()返回结果值，这个值的类型可以与原CompletableFuture的结果类型不一样。
     * 其他两个Async后缀的方法不再赘述，与上述几个作用一样
     *
     * public <U> CompletableFuture<U> 	handle(BiFunction<? super T,Throwable,? extends U> fn)
     * public <U> CompletableFuture<U> 	handleAsync(BiFunction<? super T,Throwable,? extends U> fn)
     * public <U> CompletableFuture<U> 	handleAsync(BiFunction<? super T,Throwable,? extends U> fn, Executor executor)
     */
    @Test
    public void handle(){
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            return 1+2;
        }, executorService);
        CompletableFuture<Integer> handle = completableFuture.handle((T, U) -> {
            if (U != null) {
                System.out.println("发生异常 msg:" + U.getMessage());
            }
            System.out.println("输出结果值：" + T);
            return 1 + 1;
        });
        System.out.println("completableFuture 结果值："+completableFuture.join());
        System.out.println("handle 结果值："+handle.join());
    }

    /**
     * 结果转换
     * thenApply()入参是一个Function接口是函数，有一个入参和一个返回值
     * 入参是原CompletableFuture的结果，返回值可以为任意类型的转换后的结果。
     *
     * public <U> CompletableFuture<U> thenApply(Function<? super T,? extends U> fn)
     * public <U> CompletableFuture<U> thenApplyAsync(Function<? super T,? extends U> fn)
     * public <U> CompletableFuture<U> thenApplyAsync(Function<? super T,? extends U> fn, Executor executor)
     */
    @Test
    public void thenApply(){
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            return 1+2;
        }, executorService);
        CompletableFuture<String> completableFutureThenApply = completableFuture.thenApply((originalResult) -> {
            return originalResult + "+1";
        });
        System.out.println("第一阶段结果："+completableFuture.join());
        System.out.println("第二阶段结果："+completableFutureThenApply.join());
    }

    /**
     *  thenCompose() 可以进行结果转换，在返回的结果上和thenApply()有所区别， 限定了返回结果的泛型为：? extends CompletionStage<U>
     *
     * public <U> CompletableFuture<U> thenCompose(Function<? super T, ? extends CompletionStage<U>> fn);
     * public <U> CompletableFuture<U> thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> fn) ;
     * public <U> CompletableFuture<U> thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> fn, Executor executor) ;
     */
    @Test
    public void thenCompose(){
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            return 1+2;
        }, executorService);
        CompletableFuture<Integer> completableFuture1 = completableFuture.thenCompose((t) -> {
            System.out.println("拿到上个线程的结果：" + t);
            return CompletableFuture.supplyAsync(() -> {
                return 1 + t;
            }, executorService);
        });
        System.out.println("最终结果："+completableFuture1.join());
    }

    /**
     * 结果消费
     * thenApply()入参是个Consumer类型的接口函数，可以拿到第一阶段的结果去消费，没有返回值，
     *
     * public CompletionStage<Void> thenAccept(Consumer<? super T> action);
     * public CompletionStage<Void> thenAcceptAsync(Consumer<? super T> action);
     * public CompletionStage<Void> thenAcceptAsync(Consumer<? super T> action,Executor executor);
     */
    @Test
    public void thenAccept(){
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            return 1+2;
        }, executorService);
        CompletableFuture<Void> completableFuture1 = completableFuture.thenAccept((t) -> {
            System.out.println("拿到第一阶段结果：" + t);
        });
        System.out.println("查看返回值："+completableFuture1.join());
    }

    /**
     * thenAcceptBoth()可以消费2个CompletableFuture的结果
     *
     * public <U> CompletionStage<Void> thenAcceptBoth(CompletionStage<? extends U> other,BiConsumer<? super T, ? super U> action);
     * public <U> CompletionStage<Void> thenAcceptBothAsync(CompletionStage<? extends U> other,BiConsumer<? super T, ? super U> action);
     * public <U> CompletionStage<Void> thenAcceptBothAsync(CompletionStage<? extends U> other,BiConsumer<? super T, ? super U> action,     Executor executor);
     */
    @Test
    public void thenAcceptBoth(){
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(10); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace(); }
            return 1;
        }, executorService);
        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(20); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace(); }
            return 2;
        }, executorService);
        CompletableFuture<Void> completableFuture = completableFuture1.thenAcceptBoth(completableFuture2, (t1, t2) -> {
            System.out.println("completableFuture1结果：" + t1);
            System.out.println("completableFuture2结果：" + t2);
        });
        completableFuture.join();
    }

    /**
     * thenRun()会在上一阶段CompletableFuture完成计算后执行处理，但是不会使用上一阶段的结果
     *
     * public CompletionStage<Void> thenRun(Runnable action);
     * public CompletionStage<Void> thenRunAsync(Runnable action);
     * public CompletionStage<Void> thenRunAsync(Runnable action,Executor executor);
     */
    @Test
    public void thenRun(){
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(10); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace(); }
            return 1;
        }, executorService);

        CompletableFuture<Void> completableFuture = completableFuture1.thenRun(() -> {
            try { Thread.sleep(100); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace(); }
            System.out.println("thenRun()执行结束...");
        });
        completableFuture.join();
    }

    /**
     * thenCombine() 可以组合2个CompletableFuture的结果 并返回一个新的结果
     *
     * public <U,V> CompletionStage<V> thenCombine(CompletionStage<? extends U> other,BiFunction<? super T,? super U,? extends V> fn);
     * public <U,V> CompletionStage<V> thenCombineAsync(CompletionStage<? extends U> other,BiFunction<? super T,? super U,? extends V> fn);
     * public <U,V> CompletionStage<V> thenCombineAsync(CompletionStage<? extends U> other,BiFunction<? super T,? super U,? extends V> fn,Executor executor);
     */
    @Test
    public void thenCombine(){
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(10); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace(); }
            return 1;
        }, executorService);
        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(20); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace(); }
            return 2;
        }, executorService);
        CompletableFuture<Integer> completableFuture = completableFuture1.thenCombine(completableFuture2, (t1, t2) -> {
            System.out.println("completableFuture1结果：" + t1);
            System.out.println("completableFuture2结果：" + t2);
            return t1 + t2;
        });
        System.out.println("组合后结果："+completableFuture.join());
    }

    /**
     * applyToEither() 比较2个线程任务，拿到最快执行完成任务的结果，然后可以返回一个新的结果
     *
     * public <U> CompletionStage<U> applyToEither(CompletionStage<? extends T> other,Function<? super T, U> fn);
     * public <U> CompletionStage<U> applyToEitherAsync(CompletionStage<? extends T> other,Function<? super T, U> fn);
     * public <U> CompletionStage<U> applyToEitherAsync(CompletionStage<? extends T> other,Function<? super T, U> fn,Executor executor);
     */
    @Test
    public void applyToEither(){
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(10); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace(); }
            return 1;
        }, executorService);
        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(20); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace(); }
            return 2;
        }, executorService);

        CompletableFuture<Integer> completableFuture = completableFuture1.applyToEither(completableFuture2, (t1) -> {
            System.out.println("拿到最快的结果：" + t1);
            return t1 * 2;
        });
        System.out.println("最终结果:"+completableFuture.join());
    }

    /**
     * acceptEither() 拿到2个线程任务中最快完成任务的结果进行消费，无返回结果
     *
     * public CompletionStage<Void> acceptEither(CompletionStage<? extends T> other,Consumer<? super T> action);
     * public CompletionStage<Void> acceptEitherAsync(CompletionStage<? extends T> other,Consumer<? super T> action);
     * public CompletionStage<Void> acceptEitherAsync(CompletionStage<? extends T> other,Consumer<? super T> action,Executor executor);
     */
    @Test
    public void acceptEither(){
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(21); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace(); }
            return 1;
        }, executorService);
        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(20); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace(); }
            return 2;
        }, executorService);

        CompletableFuture<Void> completableFuture = completableFuture1.acceptEither(completableFuture2, (t1) -> {
            System.out.println("拿到最快的结果：" + t1);
        });
        completableFuture.join();
    }

    /**
     * runAfterEither() 2个线程任务若其中1个执行完毕则会进行处理，不关心线程任务的执行结果
     *
     * public CompletionStage<Void> runAfterEither(CompletionStage<?> other,Runnable action);
     * public CompletionStage<Void> runAfterEitherAsync(CompletionStage<?> other,Runnable action);
     * public CompletionStage<Void> runAfterEitherAsync(CompletionStage<?> other,Runnable action,Executor executor);
     */
    @Test
    public void runAfterEither(){
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(10); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace(); }
            System.out.println("线程1执行完毕");
            return 1;
        }, executorService);
        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(20); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace(); }
            System.out.println("线程2执行完毕");
            return 2;
        }, executorService);

        CompletableFuture<Void> completableFuture = completableFuture1.runAfterEither(completableFuture2, () -> {
            System.out.println("2个线程已有1个执行完成，执行后续处理...");
        });
        completableFuture.join();
    }

    /**
     * runAfterBoth() 两个线程任务都执行结束后，执行后续处理，不关心两者结果
     *
     * public CompletionStage<Void> runAfterBoth(CompletionStage<?> other,Runnable action);
     * public CompletionStage<Void> runAfterBothAsync(CompletionStage<?> other,Runnable action);
     * public CompletionStage<Void> runAfterBothAsync(CompletionStage<?> other,Runnable action,Executor executor);
     */
    @Test
    public void runAfterBoth(){
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(10); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace(); }
            System.out.println("线程1执行完毕");
            return 1;
        }, executorService);
        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(20); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace(); }
            System.out.println("线程2执行完毕");
            return 2;
        }, executorService);

        CompletableFuture<Void> completableFuture = completableFuture1.runAfterBoth(completableFuture2, () -> {
            System.out.println("2个线程都执行完成，执行后续处理...");
        });
        completableFuture.join();
    }

    /**
     * anyOf() 可以组合多个线程任务，会在任意给定的CompletableFuture完成时完成，结果与之相同。
     *
     * public static CompletableFuture<Object> anyOf(CompletableFuture<?>... cfs)
     */
    @Test
    public void anyOf(){
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(10); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace(); }

            if(1 == 1){
                throw new RuntimeException("自定义异常");
            }
            System.out.println("线程1执行完毕");
            return 1;
        }, executorService);
        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(20); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace(); }
            System.out.println("线程2执行完毕");
            return 2;
        }, executorService);
        CompletableFuture<Object> objectCompletableFuture = CompletableFuture.anyOf(completableFuture1, completableFuture2);
        System.out.println("anyOf结果：" + objectCompletableFuture.join());
    }

    /**
     * allOf() 当所有给定的CompletableFuture都完成时完成，返回一个新的CompletableFuture
     * 可以再找个新的CompletableFuture调用thenApply()时，聚合所有CompletableFuture的返回结果
     *
     * public static CompletableFuture<Void> allOf(CompletableFuture<?>... cfs)
     */
    @Test
    public void allOf(){
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(10); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace(); }
            System.out.println("线程1执行完毕，线程名：" + Thread.currentThread().getName());
            return 1;
        }, executorService);
        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(2000); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace(); }
            System.out.println("线程2执行完毕，线程名：" + Thread.currentThread().getName());
            return 2;
        }, executorService);

        List<CompletableFuture<Integer>> completableFutures = new ArrayList<>();
        completableFutures.add(completableFuture1);
        completableFutures.add(completableFuture2);
        /**
         *  allOf() 当所有给定的CompletableFuture都"完成时完成"，返回一个新的CompletableFuture
         *  通过新的completableFuture，能知道集合中的completableFuture都完成了。
         */
        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]));
        System.out.println("allOf()返回时间：" + System.currentTimeMillis());
        completableFuture.join();
        System.out.println("allOf()执行完成时间：" + System.currentTimeMillis());
        CompletableFuture<List<Integer>> listCompletableFuture = completableFuture.thenApply((t) -> {
            System.out.println("所有线程任务执行完毕...,allOf线程名：" + Thread.currentThread().getName());
            // 聚合所有线程任务结果
            return completableFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());

        });
        // 直接通过流处理，map join() 然后聚合结果，这样的话感觉和allOf()效果一样
        //List<Integer> collect = completableFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        //listCompletableFuture.join();
        System.out.println("所有任务执行完成，聚合结果：" + listCompletableFuture.join());
    }

    @Test
    public void allOfException(){
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务1 线程名：" + Thread.currentThread().getName()+"开始执行...");
            try { Thread.sleep(50); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace(); }
            System.out.println("任务1 线程名：" + Thread.currentThread().getName()+"执行完毕");
            return 1;
        }, executorService);
        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务2 线程名：" + Thread.currentThread().getName()+"开始执行...");
            try { Thread.sleep(200); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace(); }
            if (1 == 1) {
                throw new RuntimeException("测试异常");
            }
            System.out.println("任务2 线程名：" + Thread.currentThread().getName()+"执行完毕");
            return 2;
        }, executorService).exceptionally((throwable -> {
            System.out.println(Thread.currentThread().getName() + "执行发生异常");
            return 0;
        }));

        List<CompletableFuture<Integer>> completableFutures = new ArrayList<>();
        completableFutures.add(completableFuture1);
        completableFutures.add(completableFuture2);

        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]));
        CompletableFuture<List<Integer>> listCompletableFuture = completableFuture.thenApply((t) -> {
            System.out.println("所有线程任务执行完毕...,allOf线程名：" + Thread.currentThread().getName());
            // 聚合所有线程任务结果
            return completableFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());

        });
        System.out.println("所有任务执行完成，聚合结果：" + listCompletableFuture.join());
    }

    @Test
    public void allOfException2(){
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(1);
        objects.add(2);
        System.out.println(objects);
    }
}
