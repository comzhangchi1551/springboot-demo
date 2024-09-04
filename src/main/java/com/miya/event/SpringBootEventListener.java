package com.miya.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SpringBootEventListener {

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReadyEvent(){
        log.info("SpringBootEventListener ---> onApplicationReadyEvent");
    }
}
