package com.cs.struc.concurrentcorrelation.lock;

/**
 * @author benjaminChan
 * @date 2018/8/16 0016 上午 11:23
 */
public interface RedisConstant {

    long TIME_OUT = 1000;

    String CALC_AMOUNT_KEY = "calc_amount_key_%s%s";

    String API_WEB_TIME_KEY = "api_web_time_key";
    String API_WEB_COUNTER_KEY = "api_web_counter_key";
}
