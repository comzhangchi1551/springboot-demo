package com.miya.conf;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 自定义 BeanFactoryPostProcessor
 */
@Component
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println(">>>>>>>>>>>>>>> postProcessBeanFactory >>>>>>>>>>>>>>>");
    }

    @PostConstruct
    public void init(){
        System.out.println("------------------------------ PostConstruct ------------------------------");
    }
}
