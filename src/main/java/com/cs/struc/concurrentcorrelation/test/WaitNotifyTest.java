package com.cs.struc.concurrentcorrelation.test;

/**
 * @author benjaminChan
 * @date 2018/11/15 0015 上午 10:32
 * <p>
 * 线程间的通信经典案例，等待通知机制，模拟两个线程交替打印奇数和偶数
 * <p>
 *
 * 调用wait(),notify(),notifyAll()的前提都是获取了对象的锁
 * 调用wait()方法后，线程会释放锁，进入WAITING状态
 * 从wait()方法返回的前提是调用notify()方法释放锁，从而wait()方法所在的线程获取锁
 * 调用notify()方法会将在等待队列中的线程移动到同步队列中，线程状态也变成阻塞的
 *
 * join()方法利用的也是等待通知机制
 * join()方法会阻塞当前线程，直到方法的调用者执行完毕
 *
 * CountDownLatch可以实现与join()方法同样的功能，并且更加灵活
 * CountDownLatch的await方法会阻塞当前线程，直到N变成零，如果没有变成零的话就会一直阻塞在那里
 *
 */
public class WaitNotifyTest {

    private volatile boolean flag = true;
    private volatile int num = 0;

    public static void main(String[] args) {
        WaitNotifyTest waitNotifyTest = new WaitNotifyTest();

        Thread t1 = new Thread(new OddNumber(waitNotifyTest));
        Thread t2 = new Thread(new EvenNumber(waitNotifyTest));

        t1.start();
        t2.start();
    }

    private static class OddNumber implements Runnable {

        private WaitNotifyTest waitNotifyTest;

        public OddNumber(WaitNotifyTest waitNotifyTest) {
            this.waitNotifyTest = waitNotifyTest;
        }

        @Override
        public void run() {
            while (waitNotifyTest.num < 100) {
                synchronized (WaitNotifyTest.class) {
                    if (waitNotifyTest.flag) {
                        System.out.println("OddNumber get lock,num:" + waitNotifyTest.num);
                        waitNotifyTest.num++;
                        waitNotifyTest.flag = false;

                        WaitNotifyTest.class.notify();
                    } else {
                        try {
                            WaitNotifyTest.class.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }

        }
    }

    private static class EvenNumber implements Runnable {

        private WaitNotifyTest waitNotifyTest;

        public EvenNumber(WaitNotifyTest waitNotifyTest) {
            this.waitNotifyTest = waitNotifyTest;
        }

        @Override
        public void run() {
            while (waitNotifyTest.num < 100) {
                synchronized (WaitNotifyTest.class) {
                    if (!waitNotifyTest.flag) {
                        System.out.println("EvenNumber get lock,num:" + waitNotifyTest.num);
                        waitNotifyTest.num++;
                        waitNotifyTest.flag = true;
                        WaitNotifyTest.class.notify();
                    } else {
                        try {
                            WaitNotifyTest.class.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
