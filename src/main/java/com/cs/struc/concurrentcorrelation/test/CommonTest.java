package com.cs.struc.concurrentcorrelation.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author benjaminChan
 * @date 2018/11/15 0015 上午 10:59
 */
public class CommonTest {

    public static void main(String[] args) throws InterruptedException {
        CommonTest commonTest = new CommonTest();
//        testJoin();
//        testStop();
//        commonTest.testCountDownLatch();
        for (int i = 0; i < 10; i++) {
            commonTest.testVolatile();
        }
    }

    private void testVolatile() {
        Thread t0 = new Thread(new CountThread(), "t0");
        Thread t1 = new Thread(new CountThread(), "t1");
        Thread t2 = new Thread(new CountThread(), "t2");

        t0.start();
        t1.start();
        t2.start();

        try {
            t0.join();
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("count:" + atomicInteger.getAndIncrement());
    }

    public class CountThread implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                atomicInteger.getAndIncrement();
            }
        }
    }

    private volatile int count = 0;
    private AtomicInteger atomicInteger = new AtomicInteger();

    private static final int THREAD_COUNT = 3;
    private CountDownLatch countDownLatch = new CountDownLatch(THREAD_COUNT);

    private void testCountDownLatch() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < THREAD_COUNT; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());
                    try {
                        Thread.sleep(1000);//模拟业务处理
                        countDownLatch.countDown();//事情做完了就减一好了
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        }

        interruptMainThread();

        System.out.println("exe time:" + (System.currentTimeMillis() - startTime));
    }

    private void interruptMainThread() throws InterruptedException {
        countDownLatch.await();
    }

    private volatile boolean flag = true;

    private static void testStop() throws InterruptedException {
        CommonTest commonTest = new CommonTest();
        Thread t1 = new Thread(new StopThread(commonTest));
        t1.start();

        Thread.sleep(1000);

        commonTest.stopThread();
    }

    private void stopThread() {
        this.flag = false;
    }

    private static class StopThread implements Runnable {

        private CommonTest commonTest;

        public StopThread(CommonTest commonTest) {
            this.commonTest = commonTest;
        }

        @Override
        public void run() {
            while (commonTest.flag) {
                System.out.println("Stop Thread is executing.");
            }
            System.out.println("Stop Thread");
        }
    }

    private static void testJoin() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    System.out.println("t1 over");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(0);
                    System.out.println("t2 over");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        t2.start();

        t1.join();//阻塞现场，直到调用的线程执行完毕，注意，阻塞的是当前所在的线程
        t2.join();

        System.out.println("main over!");
        System.out.println(System.currentTimeMillis() - startTime);
    }
}
