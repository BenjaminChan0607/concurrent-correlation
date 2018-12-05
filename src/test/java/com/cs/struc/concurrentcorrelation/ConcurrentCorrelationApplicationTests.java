package com.cs.struc.concurrentcorrelation;

import com.cs.struc.concurrentcorrelation.lock.UserAmountComponent;
import com.cs.struc.concurrentcorrelation.ssm.controller.OrderController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConcurrentCorrelationApplicationTests {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserAmountComponent userAmountComponent;

    private static final String LOCK_TEST = "lock_test";
    private static final int THREAD_NUM = 100;
    private CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUM);
    @Autowired
    private OrderController orderController;

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
//        testContinuous();
//        testCountDownLatch();
//        testExecutorService();
        testCyclicBarrier();

    }

    public class SSMTask implements Runnable {
        private CyclicBarrier cyclicBarrier;

        public SSMTask(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            try {
                // 等待所有任务准备就绪
                cyclicBarrier.await();
                System.out.println(Thread.currentThread().getName() + "----start execute----");
                orderController.createWrongOrder(1);
                System.out.println(Thread.currentThread().getName() + "----finish execute----");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void testCyclicBarrier() throws InterruptedException {
        int count = 100;//10万并发
        CyclicBarrier cyclicBarrier = new CyclicBarrier(count);
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        long now = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            executorService.execute(new SSMTask(cyclicBarrier));
        }

        executorService.shutdown();
        while (!executorService.isTerminated()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("All is finished!---------" + (end - now));
        TimeUnit.SECONDS.sleep(1);
    }

    private void testExecutorService() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUM);
        for (int i = 0; i < THREAD_NUM; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + "----start execute----");
                    orderController.createWrongOrder(1);
                    System.out.println(Thread.currentThread().getName() + "----finish execute----");
                }
            });
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            TimeUnit.SECONDS.sleep(1);
        }
        TimeUnit.SECONDS.sleep(1);
    }

    private void testCountDownLatch() {
        for (int i = 0; i < THREAD_NUM; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("----start execute----");
                    orderController.createWrongOrder(1);
                    countDownLatch.countDown();
                    System.out.println(Thread.currentThread().getName() + "finish");
                }
            });
            thread.start();
        }

        try {
            countDownLatch.await();
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void testContinuous() {
        for (int i = 0; i < THREAD_NUM; i++) {
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
        for (int i = 0; i < THREAD_NUM; i++) {
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
