package com.miya.rocketmq.order;

import lombok.SneakyThrows;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class BaseOrderConsumer {
    @SneakyThrows
    public static void main(String[] args) {
        // 创建消费者对象
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("my-order-producer-group1");
        // 设置nameServer地址
        consumer.setNamesrvAddr("192.168.31.208:9876");

        // 订阅主题：topic；和过滤消息用的tag表达式；
        consumer.subscribe("MyOrderTopic", "*");

        // 创建一个监听器，当broker把消息推过来时，调用；
        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                for (MessageExt msg : msgs) {
                    System.out.println(String.format("收到的消息：[%s]", new String(msg.getBody())));
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });

        // 启动消费者
        consumer.start();
        System.out.println("消费者已启动！！！");
    }
}
