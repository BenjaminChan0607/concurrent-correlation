package com.cs.struc.concurrentcorrelation.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author benjaminChan
 * @date 2018/10/11 0011 上午 10:03
 */
public class FutureTaskTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();
        // 模拟数据List
        List<String> list = new ArrayList<String>();
        for (int i = 1; i <= 1000000; i++) {
            list.add(i + "");
        }
        int singleThreadHandleNum = 100000;
        int dataSize = list.size();
        // 线程数
        int threadNum = dataSize / singleThreadHandleNum + 1;

        boolean special = dataSize % singleThreadHandleNum == 0;
        // 创建一个线程池
        ExecutorService exec = Executors.newFixedThreadPool(threadNum);
        // 定义一个任务集合
        List<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();
        Callable<Integer> task;
        List<String> cutList;
        // 确定每条线程的数据
        for (int i = 0; i < threadNum; i++) {
            if (i == threadNum - 1) {
                if (special) {
                    break;
                }
                cutList = list.subList(singleThreadHandleNum * i, dataSize);
            } else {
                cutList = list.subList(singleThreadHandleNum * i, singleThreadHandleNum * (i + 1));
            }
            final List<String> listStr = cutList;
            task = new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    System.out.println(Thread.currentThread().getName() + "线程：" + listStr);
                    return 1;
                }
            };
            // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
            tasks.add(task);
        }

        List<Future<Integer>> results = exec.invokeAll(tasks);
        for (Future<Integer> future : results) {
            System.out.println(future.get());
        }
        // 关闭线程池
        exec.shutdown();
        System.err.println("执行任务消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒");
    }
}
