package com.miya.mq.rabbit;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMQConsumer {

    @RabbitListener(queues = "queue")
    public void receive(Message msg){
        System.out.println("msg.getBody() = " + JSON.toJSONString(msg.getBody()));
        log.info("接受消息：[{}]", JSON.toJSONString(msg));
    }
}
