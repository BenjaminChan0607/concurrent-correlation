package com.cs.struc.concurrentcorrelation.lock;

/**
 * @author benjaminChan
 * @date 2018/8/16 0016 上午 11:23
 */
public interface RedisConstant {

    long TIME_OUT = 6000;//1 min

    String CALC_AMOUNT_KEY = "calc_amount_key_%s%s";
}
