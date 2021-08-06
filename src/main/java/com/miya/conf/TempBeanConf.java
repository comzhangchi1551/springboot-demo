package com.miya.conf;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import com.miya.common.TempInterceptor;
import com.miya.common.anno.CurrentUserInfoResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Auth: 张
 * Desc:
 * Date: 2021/2/22 21:21
 */
@Configuration
@Slf4j
public class TempBeanConf implements WebMvcConfigurer {

    @Autowired
    private CurrentUserInfoResolver currentUserInfoResolver;

    /**
     * 拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TempInterceptor()).addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentUserInfoResolver);
    }

    /**
     * mybatis-plus
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        // paginationInterceptor.setLimit(500);
        // 开启 count 的 join 优化,只针对部分 left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        return paginationInterceptor;
    }



    /**
     * redisTemplate
     * @param redisConnectionFactory
     * @return
     */
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//
//        // Json序列化配置
//        Jackson2JsonRedisSerializer<Object> jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jsonRedisSerializer.setObjectMapper(objectMapper);
//
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//        redisTemplate.setKeySerializer(stringRedisSerializer);
//        redisTemplate.setHashKeySerializer(stringRedisSerializer);
//
//        redisTemplate.setValueSerializer(jsonRedisSerializer);
//        redisTemplate.setHashValueSerializer(jsonRedisSerializer);
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//    }


//    @Value("${spring.rocketmq.producer.group}")
//    private String consumerGroup;
//
//
//    @Value("${spring.rocketmq.name-server}")
//    private String namesrvAddr;
//
//    @Bean
//    public DefaultMQProducer defaultMQProducer() throws MQClientException {
//        DefaultMQProducer defaultMQProducer = new DefaultMQProducer();
//        defaultMQProducer.setProducerGroup(consumerGroup);
//        defaultMQProducer.setNamesrvAddr(namesrvAddr);
//        defaultMQProducer.start();
//        return defaultMQProducer;
//    }
}
