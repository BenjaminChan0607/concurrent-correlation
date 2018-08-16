package com.cs.struc.concurrentcorrelation.lock;

import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.TIMEOUT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author benjaminChan
 * @date 2018/8/16 0016 上午 11:22
 */
@Component
@Slf4j
public class UserAmountComponent {

    @Autowired
    private RedisDistributeLock redisDistributeLock;

    public void calcAmount(Long appId, Long userId) {
        String key = String.format(RedisConstant.CALC_AMOUNT_KEY, appId, userId);

        boolean flag = redisDistributeLock.tryLock(key, RedisConstant.TIME_OUT);
        try {
            if (!flag) {
                throw new Exception("get lock fail");
            }
//            在获取到锁之后，当前线程可以开始自己的业务处理，当处理完毕后，比较自己的处理时间和对于锁设置的超时时间，如果小于锁设置的超时时间，则直接执行delete释放锁；如果大于锁设置的超时时间，则不需要再锁进行处理。
            long startTime = System.currentTimeMillis();
            calcUserAmountBiz();
            if (System.currentTimeMillis() - startTime < RedisConstant.TIME_OUT) {
                redisDistributeLock.releaseLock(key);
            }
        } catch (Exception e) {
            log.info("get lock fail", e);
        } finally {
            redisDistributeLock.releaseLock(key);
        }
    }

    private void calcUserAmountBiz() {
    }
}
