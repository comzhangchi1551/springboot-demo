package com.miya.rocketmq.order;


import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;

@Slf4j
public class BaseOrderProducer {

    public static void main(String[] args) throws Exception {

        DefaultMQProducer producer = new DefaultMQProducer("my-order-producer-group1");

        producer.setNamesrvAddr("192.168.202.101:9876");

        producer.start();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
                // 创建消息
                Message message = new Message("MyOrderTopic", "TagA", ("hello zcc : " + i + " : " + j).getBytes(StandardCharsets.UTF_8));

                // selectMessageQueueByHash 意思是使用 hash 来控制 queue 的投放，确保同一类消息，能投放到一个 queue 中。
                SendResult send = producer.send(message, new SelectMessageQueueByHash(), i);
                log.info("发送消息： " + send);
            }

        }

        producer.shutdown();


    }
}
