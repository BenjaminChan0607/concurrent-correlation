package com.cs.struc.concurrentcorrelation.ssm.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author benjaminChan
 * @date 2018/12/5 0005 下午 5:22
 */
@RestController
@Slf4j
public class ThreadPoolProducerController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final int CALC_NUM = 10000;

    @RequestMapping("/threadPoolProducer")
    public void threadPoolConsumer() {
        for (int i = 0; i < CALC_NUM; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("appId", "zzz");
            map.put("userId", 1001L);
            rabbitTemplate.convertAndSend("thread_pool_test", JSON.toJSONString(map));
            System.out.println("threadPoolProducer " + JSON.toJSONString(map));
        }

    }
}
