package com.cs.struc.concurrentcorrelation;

import com.cs.struc.concurrentcorrelation.handler.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author benjaminChan
 * @date 2018/12/11 0011 下午 5:57
 */
@Component
@Slf4j
public class EventListenerRunner implements CommandLineRunner {

    @Autowired
    private LoginListener loginListener;
    @Autowired
    private RegisterListener registerListener;
    @Autowired
    private LivenessListener livenessListener;

    @Override
    public void run(String... args) throws Exception {
        registerAllListener();
    }

    private void registerAllListener() {
        EventListenerManager.getInstance().registerEventListener(LoginEvent.class, loginListener);
        EventListenerManager.getInstance().registerEventListener(RegisterEvent.class, registerListener);
        EventListenerManager.getInstance().registerEventListener(LivenessEvent.class, livenessListener);
        log.info("EventListener num:{}", EventListenerManager.getInstance().eventListenerMap.size());
    }

}