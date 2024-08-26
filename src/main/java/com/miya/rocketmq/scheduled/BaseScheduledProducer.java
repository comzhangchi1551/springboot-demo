package com.miya.rocketmq.scheduled;


import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;

/**
 * 官网都有示例，这里只是做个demo
 */
public class BaseScheduledProducer {

    public static void main(String[] args) throws Exception {
        // 创建生产者；
        DefaultMQProducer producer = new DefaultMQProducer("my-scheduled-producer-group");
        // 指定namerServer地址；
        producer.setNamesrvAddr("192.168.202.101:9876");

        // 启动生产者
        producer.start();

        for (int i = 0; i < 10; i++) {
            // 创建消息
            Message message = new Message("MyScheduledTopic", "TagA", "hello zcc111444".getBytes(StandardCharsets.UTF_8));

            // 等级3为10s，具体等级代表的时间，可以看官网；
            message.setDelayTimeLevel(3);

            // 发送同步消息，同步阻塞等待；
            SendResult send = producer.send(message);

            System.out.println(send);
        }

        // 关闭生产者
        producer.shutdown();


    }
}
