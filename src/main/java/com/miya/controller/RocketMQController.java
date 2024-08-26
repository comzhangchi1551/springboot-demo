package com.miya.controller;

import com.miya.common.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("rocketmq")
@RocketMQMessageListener(topic = "springboot-test-topic", consumerGroup = "springboot-demo-consumer-group")
public class RocketMQController implements RocketMQListener<String> {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    private static final String TOPIC = "springboot-test-topic";

    @RequestMapping("producer")
    public BaseResult<String> producer (@RequestParam String msg) {
        rocketMQTemplate.convertAndSend(TOPIC, msg);
        return BaseResult.success("success");
    }


    @Override
    public void onMessage(String s) {
        log.info("consumer --> msg = {}", s);
    }
}
