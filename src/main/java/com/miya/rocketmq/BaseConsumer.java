package com.miya.rocketmq;

import lombok.SneakyThrows;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class BaseConsumer {
    @SneakyThrows
    public static void main(String[] args) {
        // 创建消费者对象
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("my-producer-group1");
        // 设置nameServer地址
        consumer.setNamesrvAddr("192.168.31.208:9876");

        // 订阅主题：topic；和过滤消息用的tag表达式；
        consumer.subscribe("MyTopic1", "*");

        // 创建一个监听器，当broker把消息推过来时，调用；
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

                for (MessageExt msg : msgs) {
                    System.out.println("收到的消息：" + new String(msg.getBody()));
                    System.out.println("收到的消息222：" + String.valueOf(msg.getBody()));
                }

                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        // 启动消费者
        consumer.start();
        System.out.println("消费者已启动！！！");
    }
}
