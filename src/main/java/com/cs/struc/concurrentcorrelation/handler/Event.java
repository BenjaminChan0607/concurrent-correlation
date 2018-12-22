package com.cs.struc.concurrentcorrelation.handler;

/**
 * @author benjaminChan
 * @date 2018/12/11 0011 下午 7:04
 */
public abstract class Event {
    private Object object;

    public Event(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
