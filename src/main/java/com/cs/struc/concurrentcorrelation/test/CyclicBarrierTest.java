package com.cs.struc.concurrentcorrelation.test;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author benjaminChan
 * @date 2018/12/29 0029 上午 11:39
 */
public class CyclicBarrierTest {

    public static void main(String[] args) {
        CyclicBarrierTest cyclicBarrierTest = new CyclicBarrierTest();
        cyclicBarrierTest.testCyclicBarrier();
    }

    private static final int THREAD_NUM = 5;
    private CyclicBarrier cyclicBarrier = new CyclicBarrier(THREAD_NUM);

    private void testCyclicBarrier() {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUM);
        for (int i = 0; i < THREAD_NUM; i++) {
            executorService.submit(new RunnerThread());
        }
        executorService.shutdown();
    }

    private class RunnerThread implements Runnable {
        @Override
        public void run() {
            System.out.println("Runner pre");
            try {
                cyclicBarrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Runner post");
        }
    }

}
