package ITEM81;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Example {
    public static void main(String[] args) throws InterruptedException {
        ConcurrentMap<String, String> concurrentMap = new ConcurrentHashMap<>();
        Map<String, String> map = new HashMap<>();

        map.putIfAbsent("K", "V");
        concurrentMap.get("test");

        ExecutorService exec = Executors.newFixedThreadPool(100);
        long timer = time(exec, 3, () -> {System.out.println("action.run()");});

        System.out.println(timer);

        exec.shutdown();
    }

    public static long time(Executor executor, int concurrency, Runnable action) throws InterruptedException {
        CountDownLatch ready = new CountDownLatch(concurrency);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(concurrency);
 
        for (int i = 0; i < concurrency; i++) {
            executor.execute(() -> {
                // 타이머에게 준비를 마쳤음을 알림
                ready.countDown();
                try {
                    // 모든 작업자 스레드가 준비될때까지 기다린다.
                    System.out.println("start 기달중");
                    start.await();
                    action.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    // 타이머에게 작업을 마쳤음을 알림
                    done.countDown();
                }
            });
        }
        
        System.out.println("ready 기달중");
        ready.await(); //모든 작업자가 준비될때까지 기다려라
        long startNanos = System.nanoTime();
        System.out.println("start.countDown()");
        start.countDown(); // 모든 작업자들을 깨움
        System.out.println("done.await");
        done.await(); // 모든 작업자가 일 끝마치기를 기다림
        return System.nanoTime() - startNanos;
        
    }

}