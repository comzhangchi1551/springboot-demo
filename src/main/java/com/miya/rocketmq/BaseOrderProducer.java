package com.miya.rocketmq;


import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;

public class BaseOrderProducer {

    public static void main(String[] args) throws Exception {
        // 创建生产者；
        DefaultMQProducer producer = new DefaultMQProducer("my-order-producer-group1");
        // 指定namerServer地址；
        producer.setNamesrvAddr("192.168.31.208:9876");

        // 启动生产者
        producer.start();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
                // 创建消息
                Message message = new Message("MyOrderTopic", "TagA", ("hello zcc : " + i + " : " + j).getBytes(StandardCharsets.UTF_8));
                // 发送消息
                SendResult send = producer.send(message, new SelectMessageQueueByHash(), i);
                System.out.println("发送消息： " + send);
            }

        }

        // 关闭生产者
        producer.shutdown();


    }
}
