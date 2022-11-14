package com.miya.conf;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * 自定义 BeanFactoryPostProcessor
 */
@Component
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor, InitializingBean {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("---------------->>>>>>>>>>>>>>>");
    }

    public void t1(){
        System.out.println("eeee");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("------------------------------<<<<<<<<<<<<<<<<<<<");
    }
}
