package com.miya.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyEventHandler {

    @EventListener
    public void execute(MyEvent myEvent){
        log.info("myEvent ---> [{}]", myEvent.toString());
    }


    @EventListener
    public void execute2(MyEvent myEvent){
        log.info("myEvent222 ---> [{}]", myEvent.toString());
    }
}
