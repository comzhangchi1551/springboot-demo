package com.miya.common.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * 自定义 BeanFactoryPostProcessor
 */
@Component
public class LocalBeanFactoryPostProcessor implements BeanFactoryPostProcessor, InitializingBean {


    /**
     * beanFactory在生成BeanDifinitionMap后，会调用此方法；（只调一次）
     * @param beanFactory the bean factory used by the application context
     * @throws BeansException
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("----------------postProcessBeanFactory----------------");
    }


    /**
     * 当前Bean完成后，调用
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("===LocalBeanFactoryPostProcessor===afterPropertiesSet===");
    }
}
