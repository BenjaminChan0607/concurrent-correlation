package com.cs.struc.concurrentcorrelation.handler;

import org.springframework.stereotype.Component;

/**
 * @author benjaminChan
 * @date 2018/12/11 0011 下午 7:30
 */
@Component
public class RegisterListener implements EventListener {
    @Override
    public void listen(Event event) {
        System.out.println(getClass().getSimpleName() + " listen event");
    }
}
