package com.cs.struc.concurrentcorrelation.ssm.controller;

import com.cs.struc.concurrentcorrelation.ssm.service.StockOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author benjaminChan
 * @date 2018/12/4 0004 下午 5:34
 */
@RestController
@Slf4j
public class OrderController {

    @Autowired
    private StockOrderService stockOrderService;

    @RequestMapping("/createWrongOrder/{sid}")
    @ResponseBody
    public String createWrongOrder(@PathVariable int sid) {
        log.info("sid=[{}]", sid);
        int id = 0;
        try {
            id = stockOrderService.createWrongOrder(sid);
        } catch (Exception e) {
            log.error("Exception", e);
        }
        return String.valueOf(id);
    }

    @RequestMapping("/createWrongOrder2")
    @ResponseBody
    public void createOptimisticLimitOrder() {
        int count = 300;//10万并发
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
                stockOrderService.createWrongOrder(1);
                System.out.println(Thread.currentThread().getName() + "----finish execute----");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
