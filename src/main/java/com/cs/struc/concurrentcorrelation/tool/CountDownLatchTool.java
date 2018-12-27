package com.cs.struc.concurrentcorrelation.tool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author benjaminChan
 * @date 2018/8/15 0015 上午 11:28
 * <p>
 * 准许一个或者多个线程等待其他线程完成操作
 */
public class CountDownLatchTool {

    public static void main(String[] args) throws InterruptedException {
        new CountDownLatchTool().test();
    }

    private void test() {
        ThreadTest threadTest = new ThreadTest();
        threadTest.start();

        long startTime = System.currentTimeMillis();
        threadTest.startWait();
        System.out.println(System.currentTimeMillis() - startTime);

        System.out.println(4);
    }

    public class ThreadTest extends Thread {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println(1 + "," + Thread.currentThread().getName());
//                countDownLatch.countDown();
                TimeUnit.SECONDS.sleep(2);
                System.out.println(2 + "," + Thread.currentThread().getName());
//                countDownLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void startWait() {
            try {
                countDownLatch.await(1, TimeUnit.SECONDS);
                System.out.println(3 + "," + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}