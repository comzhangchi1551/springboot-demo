package com.miya.controller;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("kafka/test")
public class KafkaTestController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    public static final String ABC_TEST_TOPIC = "abctest";

    @RequestMapping("send")
    public String sendMq(@RequestParam String key, @RequestParam String msg){
        kafkaTemplate.send(ABC_TEST_TOPIC, key, msg);
        return "success";
    }


    @KafkaListener(topics = ABC_TEST_TOPIC)
    public void listener(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String value = record.value();
        System.out.println("listener ----> " + value);
        System.out.println(record);
        //手动提交offset
        ack.acknowledge();
    }


}
