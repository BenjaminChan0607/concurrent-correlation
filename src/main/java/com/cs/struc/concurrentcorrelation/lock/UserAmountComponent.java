package com.cs.struc.concurrentcorrelation.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author benjaminChan
 * @date 2018/8/16 0016 上午 11:22
 */
@Component
@Slf4j
public class UserAmountComponent {

    @Autowired
    private RedisDistributeLock redisDistributeLock;

    public void calcAmount(String appId, Long userId) {
        String key = String.format(RedisConstant.CALC_AMOUNT_KEY, appId, userId);

        boolean flag = redisDistributeLock.tryLock(key, RedisConstant.TIME_OUT);
        try {
            if (!flag) {
                throw new Exception();
            }
            calcUserAmountBiz();
            redisDistributeLock.releaseLock(key);
        } catch (Exception e) {
            log.warn("get lock fail,appId:{},userId:{}", appId, userId);
        } finally {
        }
    }

    private void calcUserAmountBiz() {
        try {
            TimeUnit.SECONDS.sleep(2);
            System.out.println(Thread.currentThread().getName() + " calc amount over!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
