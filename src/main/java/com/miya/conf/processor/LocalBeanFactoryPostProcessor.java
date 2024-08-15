package com.miya.conf.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.PostConstruct;


/**
 * 注意：实现了BeanFactoryPostProcessor之后，@PostConstruct、@PreDestroy和@Value注解，都会失效；
 */
@Slf4j
@Configuration
@Primary
public class LocalBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    /**
     * 由于实现了 BeanFactoryPostProcessor，此方法不会生效
     */
    @Value("${server.port}")
    private String serverPort;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.info("LocalBeanFactoryPostProcessor ===> postProcessBeanFactory");
    }


    @PostConstruct
    public void init(){
        log.info("由于实现了 BeanFactoryPostProcessor，此方法不会生效");
    }
}
