package com.miya.entity;

import com.miya.entity.model.TempUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LocalBeanPostProcessor2 implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass() == TempUser.class) {
            log.info("Before ---> TempUser22222");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass() == TempUser.class) {
            log.info("After ---> TempUser22222");
        }
        return bean;
    }
}
