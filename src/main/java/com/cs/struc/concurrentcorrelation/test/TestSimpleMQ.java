package com.cs.struc.concurrentcorrelation.test;

import java.util.Random;

/**
 * @author benjaminChan
 * @date 2018/12/29 0029 下午 5:46
 */
public class TestSimpleMQ {

    private static final int THREAD_NUM = 10;
    private static final int BOUND = 5;
    private static final Random random = new Random(System.currentTimeMillis());

    public static void main(String[] args) throws InterruptedException {

        SimpleMQ queue = new SimpleMQ(BOUND);

        for (int i = 1; i <= THREAD_NUM; i++) {
            Thread thread = new Thread(new Producer(queue), String.valueOf(i));
            thread.start();
        }

        for (int i = 1; i <= THREAD_NUM; i++) {
            Thread thread = new Thread(new Consumer(queue), String.valueOf(i));
            thread.start();
        }

    }

    static class Producer implements Runnable {
        private SimpleMQ queue;

        public Producer(SimpleMQ queue) {
            this.queue = queue;
        }

        public void produce() throws InterruptedException {
            queue.produce(new Integer(random.nextInt(100)));
        }

        @Override
        public void run() {
            try {
                produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Consumer implements Runnable {
        private SimpleMQ queue;

        public Consumer(SimpleMQ queue) {
            this.queue = queue;
        }

        public void remove() throws InterruptedException {
            queue.consume();
        }

        @Override
        public void run() {
            try {
                remove();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
