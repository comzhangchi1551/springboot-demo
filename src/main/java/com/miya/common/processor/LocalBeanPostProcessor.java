package com.miya.common.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LocalBeanPostProcessor implements BeanPostProcessor, InitializingBean {

    /**
     * 每一个Bean在调用init方法之前都会被此方法切入；
     * @param bean the new bean instance
     * @param beanName the name of the bean
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        log.info("===postProcessBeforeInitialization=== ---> " + bean.getClass().getName());
        return bean;
    }


    /**
     * 每一个Bean在调用init方法之后，都会被此方法切入；
     * @param bean the new bean instance
     * @param beanName the name of the bean
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        log.info("===postProcessAfterInitialization=== ---> " + bean.getClass().getName());
        return bean;
    }



    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("===LocalBeanPostProcessor===afterPropertiesSet===");
    }
}
