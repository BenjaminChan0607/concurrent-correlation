package com.cs.struc.concurrentcorrelation;

import com.cs.struc.concurrentcorrelation.lock.UserAmountComponent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConcurrentCorrelationApplicationTests {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserAmountComponent userAmountComponent;

    private static final String LOCK_TEST = "lock_test";
    private static final int THREAD_COUNT = 10;
    private CountDownLatch countDownLatch = new CountDownLatch(THREAD_COUNT);

    private class AmountThread implements Runnable {

        @Override
        public void run() {
            userAmountComponent.calcAmount("zzz", 1001L);
            countDownLatch.countDown();
        }
    }

    @Test
    public void contextLoads() throws InterruptedException {
//        testConcurrent();
        testContinuous();
    }

    private void testContinuous() {
        for (int i = 0; i < THREAD_COUNT; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    userAmountComponent.calcAmount("zzz", 1001L);
                }
            }).start();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void testConcurrent() throws InterruptedException {
        for (int i = 0; i < THREAD_COUNT; i++) {
            Thread t = new Thread(new AmountThread());
            t.start();
        }
        countDownLatch.await();
        TimeUnit.SECONDS.sleep(6);
    }

    @Test
    public void setIfAbsent() {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        boolean flag = valueOperations.setIfAbsent("1", "1");
        System.out.println(flag);
        System.out.println(valueOperations.get("1"));

        System.out.println(valueOperations.setIfAbsent("1", "2"));
        System.out.println(valueOperations.get("1"));
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
