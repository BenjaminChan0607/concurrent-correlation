package com.cs.struc.concurrentcorrelation.test;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author benjaminChan
 * @date 2019/1/2 0002 上午 11:40
 *
 * SimpleMQ:include basic producer and consumer
 *
 */
public class SimpleMQ {
    private Integer[] integers;
    private ReentrantLock reentrantLock = new ReentrantLock();
    private Condition producer = reentrantLock.newCondition();
    private Condition consumer = reentrantLock.newCondition();

    private volatile int count, addIndex, delIndex;

    public SimpleMQ(int size) {
        this.integers = new Integer[size];
    }

    public void produce(Integer integer) throws InterruptedException {
        reentrantLock.lock();
        try {
            if (count == integers.length) {
                producer.await();
            }
            integers[addIndex] = integer;
            if (++addIndex == integers.length) {
                addIndex = 0;
            }
            count++;
            System.out.println(Thread.currentThread() + "," + Arrays.toString(integers));
            producer.signal();
        } finally {
            reentrantLock.unlock();
        }
    }

    public void consume() throws InterruptedException {
        reentrantLock.lock();
        try {
            if (count == 0) {
                consumer.await();
            }
            integers[delIndex] = null;
            if (++delIndex == integers.length) {
                delIndex = 0;
            }
            count--;
            System.out.println(Thread.currentThread() + "," + Arrays.toString(integers));
            consumer.signal();
        } finally {
            reentrantLock.unlock();
        }
    }
}
