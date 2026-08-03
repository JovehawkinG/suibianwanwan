package practice.juc;

import java.util.concurrent.*;

// 创建方式
public class C_01_createThread {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // Thread类
        class MyThread extends Thread {
            @Override
            public void run() {
                System.out.println("********newThread1*********");
            }
        }
        new MyThread().start();

        // Runnable接口
        Thread thread = new Thread(() -> System.out.println("********newThread2*********"));
        thread.start();

        // Callable接口
        FutureTask<String> future = new FutureTask<>(() -> "********newThread3*********");
        new Thread(future).start();
        System.out.println(future.get());

        // 线程池
        Executors.newFixedThreadPool(4).execute(() -> System.out.println("********newThread4*********"));

        // 异步编排
        CompletableFuture.runAsync(() -> System.out.println("********newThread5*********"));
    }
}
