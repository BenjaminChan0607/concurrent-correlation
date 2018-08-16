package com.cs.struc.concurrentcorrelation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConcurrentCorrelationApplicationTests {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String LOCK_TEST = "lock_test";

    @Test
    public void contextLoads() {
        getAndSet();
    }

    public void getAndSet() {
        redisTemplate.opsForValue().set(LOCK_TEST, "123");
        String str2 = redisTemplate.opsForValue().getAndSet(LOCK_TEST, LOCK_TEST);
        System.out.println(str2);
        System.out.println(redisTemplate.opsForValue().get(LOCK_TEST));

        String str = redisTemplate.opsForValue().getAndSet("test", "test");
        System.out.println(str);
        System.out.println(redisTemplate.opsForValue().get("test"));
    }
}
