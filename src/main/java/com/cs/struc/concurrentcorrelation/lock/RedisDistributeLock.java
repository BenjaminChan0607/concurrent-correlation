package com.cs.struc.concurrentcorrelation.lock;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

/**
 * @author benjaminChan
 * @date 2018/8/16 0016 上午 9:46
 * <p>
 * 使用redis的setIfAbsent()、get()、getset()方法，用于分布式锁
 * 1. setnx(lockkey, 当前时间+过期超时时间) ，如果返回1，则获取锁成功；如果返回0则没有获取到锁，转向2。
 * 2. get(lockkey)获取值oldExpireTime ，并将这个value值与当前的系统时间进行比较，如果小于当前系统时间，则认为这个锁已经超时，转向3。
 * 3. 计算newExpireTime=当前时间+过期超时时间，然后getset(lockkey, newExpireTime) 会返回当前lockkey的值currentExpireTime。
 * 4. 判断currentExpireTime与oldExpireTime 是否相等，如果相等，说明当前getset设置成功，获取到了锁。如果不相等，说明这个锁又被别的请求获取走了，那么当前请求可以直接返回失败，或者继续重试。
 * 5. 在获取到锁之后，当前线程可以开始自己的业务处理，当处理完毕后，比较自己的处理时间和对于锁设置的超时时间，如果小于锁设置的超时时间，则直接执行delete释放锁；如果大于锁设置的超时时间，则不需要再锁进行处理。
 */
@Component
@Slf4j
public class RedisDistributeLock implements DistributeLock {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean tryLock(String key, long timeout) {
        Validate.notNull(key, "key不为空");

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String expireTime = String.valueOf(System.currentTimeMillis() + timeout);
        boolean flag = valueOperations.setIfAbsent(key, expireTime);
        if (flag) {
            //初次获取锁
            System.out.println(Thread.currentThread().getName() + " 初次获取锁");
            return true;
        }

        String oldExpireTime = valueOperations.get(key);
        if (Long.valueOf(oldExpireTime) < System.currentTimeMillis()) {
            //锁已经超时了，准备重新获取锁
            System.out.println(Thread.currentThread().getName() + " 锁已经超时了，准备重新获取锁");
            long newExpireTime = System.currentTimeMillis() + timeout;
            String currentExpireTime = valueOperations.getAndSet(key, String.valueOf(newExpireTime));
            if (currentExpireTime == null || currentExpireTime.equals(oldExpireTime)) {
                System.out.println(Thread.currentThread().getName() + " 锁超时后获取锁");
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public void releaseLock(String key) {
        releaseLock(key, false);
    }

    @Override
    public void releaseLock(String key, boolean force) {
        Validate.notNull(key, "key不为空");

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String value = valueOperations.get(key);
        if (StringUtils.isBlank(value)) {
            log.info("lock is not exists");
            return;
        }
        redisTemplate.delete(key);
        System.out.println(Thread.currentThread().getName() + " delete key:" + key);
    }
}
