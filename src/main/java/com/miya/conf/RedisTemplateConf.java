//package com.miya.conf;
//
//import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.serializer.GenericToStringSerializer;
//
//@Configuration
//public class RedisTemplateConf {
//
//    @Bean
//    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        // 使用 GenericFastJsonRedisSerializer 替换默认序列化
//        GenericFastJsonRedisSerializer genericFastJsonRedisSerializer = new GenericFastJsonRedisSerializer();
//        // 设置key和value的序列化规则
//        redisTemplate.setKeySerializer(new GenericToStringSerializer<>(Object.class));
//        redisTemplate.setValueSerializer(genericFastJsonRedisSerializer);
//        // 设置hashKey和hashValue的序列化规则
//        redisTemplate.setHashKeySerializer(new GenericToStringSerializer<>(Object.class));
//        redisTemplate.setHashValueSerializer(genericFastJsonRedisSerializer);
//        // 设置支持事物
//        redisTemplate.setEnableTransactionSupport(true);
//        redisTemplate.afterPropertiesSet();
//        System.out.println(redisTemplate);
//        return redisTemplate;
//    }
//
//    @Bean
//    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory){
//        StringRedisTemplate redisTemplate = new StringRedisTemplate();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        // 使用 GenericFastJsonRedisSerializer 替换默认序列化
//        GenericFastJsonRedisSerializer genericFastJsonRedisSerializer = new GenericFastJsonRedisSerializer();
//        // 设置key和value的序列化规则
//        redisTemplate.setKeySerializer(new GenericToStringSerializer<>(Object.class));
//        redisTemplate.setValueSerializer(genericFastJsonRedisSerializer);
//        // 设置hashKey和hashValue的序列化规则
//        redisTemplate.setHashKeySerializer(new GenericToStringSerializer<>(Object.class));
//        redisTemplate.setHashValueSerializer(genericFastJsonRedisSerializer);
//        // 设置支持事物
//        redisTemplate.setEnableTransactionSupport(true);
//        redisTemplate.afterPropertiesSet();
//        System.out.println(redisTemplate);
//        return redisTemplate;
//    }
//}
