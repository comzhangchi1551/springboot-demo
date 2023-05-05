package com.miya.conf;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConf {

    @Bean
    public Queue queue(){
        return new Queue("queue", true);
    }
}
