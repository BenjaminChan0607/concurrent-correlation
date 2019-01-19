package com.cs.struc.concurrentcorrelation.ssm.controller;

import com.cs.struc.concurrentcorrelation.lock.RedisConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author benjaminChan
 * @date 2018/12/24 0024 下午 2:45
 */
@RestController
public class SimpleDistributeLimitController {

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/testDomainName")
    public String execute() {
        return "domainName";
    }

    @RequestMapping("/testDistributeLimit")
    public void testDistributeLimit() {
        if (!redisTemplate.hasKey(RedisConstant.API_WEB_TIME_KEY)) {
            redisTemplate.opsForValue().set(RedisConstant.API_WEB_TIME_KEY, RedisConstant.API_WEB_TIME_KEY, 1, TimeUnit.SECONDS);
            redisTemplate.opsForValue().set(RedisConstant.API_WEB_COUNTER_KEY, 0, 10, TimeUnit.SECONDS);
//            incr(RedisConstant.API_WEB_COUNTER_KEY,1);
        }

        String timeValue = (String) redisTemplate.opsForValue().get(RedisConstant.API_WEB_TIME_KEY);
        if (StringUtils.isNotBlank(timeValue) && incr(RedisConstant.API_WEB_COUNTER_KEY, 1) > 10) {
            System.out.println("超标了");
            return;
        }
        System.out.println("testDistributeLimit " + Thread.currentThread().getName());
    }

    public Long incr(String key, long liveTime) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        Long increment = entityIdCounter.getAndIncrement();

        if ((null == increment || increment.longValue() == 0) && liveTime > 0) {//初始设置过期时间
            entityIdCounter.expire(liveTime, TimeUnit.SECONDS);
        }

        return increment;
    }
}
