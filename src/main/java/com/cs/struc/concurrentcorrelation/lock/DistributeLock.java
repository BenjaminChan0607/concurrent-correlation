package com.cs.struc.concurrentcorrelation.lock;

/**
 * @author benjaminChan
 * @date 2018/8/16 0016 上午 10:01
 */
public interface DistributeLock {

    boolean tryLock(String key, long timeout);

    void releaseLock(String key);

    void releaseLock(String key, boolean force);
}
