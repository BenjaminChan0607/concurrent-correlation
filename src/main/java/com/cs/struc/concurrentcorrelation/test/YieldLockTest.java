package com.cs.struc.concurrentcorrelation.test;

/**
 * @author benjaminChan
 * @date 2019/1/4 0004 下午 1:52
 *
 * yield() 退让资源但是仍持有锁
 * sleep() 进入阻塞状态但是不会释放锁
 */
public class YieldLockTest {

    private static Object object = new Object();

    public static void main(String[] args) {
        Thread yieldThreadA = new Thread(new YieldThread("A"));
        Thread yieldThreadB = new Thread(new YieldThread("B"));

        yieldThreadA.start();
        yieldThreadB.start();
    }

    private static class YieldThread implements Runnable {
        private String name;

        public YieldThread(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            synchronized (object) {
                for (int i = 0; i < 10; i++) {
                    System.out.println(Thread.currentThread().getName() + "," + i);
                    if (i % 4 == 0) {
                        Thread.yield();
                    }
                }
            }

        }
    }

}
