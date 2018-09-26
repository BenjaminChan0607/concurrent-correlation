package com.cs.struc.concurrentcorrelation.common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author benjaminChan
 * @date 2018/8/20 0020 上午 10:13
 * <p>
 * Runnable实现没有返回值的并发编程
 * Callable实现有返回值的并发编程
 * Future能够获取返回值
 */
public class FutureTest {
    public static class Task implements Callable<String> {
        @Override
        public String call() throws Exception {
            String tid = String.valueOf(Thread.currentThread().getId());
            System.out.printf("Thread#%s : in call\n", tid);
            return tid;
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        List<Future<String>> list = new ArrayList<Future<String>>();
        ExecutorService es = Executors.newCachedThreadPool();
        for (int i = 0; i < 100; i++)
            list.add(es.submit(new Task()));

        for (Future<String> res : list)
            System.out.println(res.get());
    }
}
