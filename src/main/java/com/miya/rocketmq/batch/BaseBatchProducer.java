package com.miya.rocketmq.batch;


import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 官网都有示例，这里只是做个demo
 */
public class BaseBatchProducer {

    public static void main(String[] args) throws Exception {
        // 创建生产者；
        DefaultMQProducer producer = new DefaultMQProducer("my-batch-producer-group");
        // 指定namerServer地址；
        producer.setNamesrvAddr("192.168.31.208:9876");

        // 启动生产者
        producer.start();

        List<Message> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            // 创建消息

            Message message = new Message("MyBatchTopic", "TagA", ("hello zcc " + i).getBytes(StandardCharsets.UTF_8));

            list.add(message);

        }

        // 批量发送
        producer.send(list);

        // 关闭生产者
        producer.shutdown();


    }
}
