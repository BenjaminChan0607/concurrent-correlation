package com.cs.struc.concurrentcorrelation.test;

import org.junit.Test;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author benjaminChan
 * @date 2018/11/15 0015 下午 4:41
 */
public class ReentrantLockTest {

    private ReentrantLock reentrantLock = new ReentrantLock();

    @Test
    public void testReentrantLock() {
        reentrantLock.lock();
        try {
            //op
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reentrantLock.unlock();
        }
    }

}
