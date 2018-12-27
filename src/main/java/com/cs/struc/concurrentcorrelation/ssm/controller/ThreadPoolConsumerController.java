package com.cs.struc.concurrentcorrelation.ssm.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author benjaminChan
 * @date 2018/12/5 0005 下午 5:22
 */
@RestController
@Slf4j
public class ThreadPoolConsumerController {

    private static final int CALC_NUM = 10;

    @Autowired
    private TaskExecutor executor;

    //    @RabbitListener(queues = "thread_pool_test")
//    @RabbitHandler
    public void threadPoolConsumerV2(String json) {
        Map<String, Object> map = (Map<String, Object>) JSON.parse(json);
        String appId = (String) map.get("appId");
        int userId = (int) map.get("userId");
        try {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    long result = 1024;
                    for (int i = 1; i < CALC_NUM; i++) {
                        result += i;
                        result -= i;
                        result *= i;
                        result /= i;
                        log.info("threadPoolConsumer " + result + "," + userId + "," + appId);
                        try {
                            TimeUnit.MICROSECONDS.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (Exception e) {
            log.error("----threadPoolConsumer----", e);
        }
    }

    //    @RabbitListener(queues = "thread_pool_test")
//    @RabbitHandler
    public void threadPoolConsumer(String json) {
        Map<String, Object> map = (Map<String, Object>) JSON.parse(json);
        String appId = (String) map.get("appId");
        int userId = (int) map.get("userId");
        try {
            long result = 1024;
            for (int i = 1; i < CALC_NUM; i++) {
                result += i;
                log.info("threadPoolConsumer " + result + "," + userId + "," + appId);
                try {
                    TimeUnit.MICROSECONDS.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            log.error("----threadPoolConsumer----", e);
        }
    }
}
