package com.miya;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class RocketMQTest {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Test
    public void producer(){
        rocketMQTemplate.convertAndSend("springboot-test-topic","enenen");
    }


    @Test
    public void consumer(){
//        rocketMQTemplate.
    }


}
