package com.cs.struc.concurrentcorrelation.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author benjaminChan
 * @date 2018/12/11 0011 下午 7:03
 */
public class EventListenerManager {
    private static EventListenerManager eventListenerManager = new EventListenerManager();

    private EventListenerManager() {
    }

    public static EventListenerManager getInstance() {
        return eventListenerManager;
    }

    public Map<Class<? extends Event>, EventListener> eventListenerMap = new ConcurrentHashMap<>();

    public void registerEventListener(Class clazz, EventListener eventListener) {
        eventListenerMap.put(clazz, eventListener);
    }

    public void unRegisterEventListener(Class clazz, EventListener eventListener) {
        eventListenerMap.remove(clazz);
    }

    public void listen(Event event) {
        EventListener eventListener = eventListenerMap.get(event.getClass());
        eventListener.listen(event);
    }
}
