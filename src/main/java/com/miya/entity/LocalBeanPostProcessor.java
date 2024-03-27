package com.miya.entity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Configuration
@Slf4j
public class LocalBeanPostProcessor implements BeanPostProcessor, InitializingBean, CommandLineRunner, ApplicationRunner {

    @Value("${server.port}")
    private String serverPort;

    // 每一个bean，都会执行
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        String packageName = Optional.ofNullable(bean).map(Object::getClass).map(Class::getPackage).map(Package::getName).orElseGet(() -> "");
//        if (packageName.startsWith("com.miya")) {
//            log.info(String.format("=== postProcessBeforeInitialization ===> bean.class = [%s]", bean.getClass()));
//            log.info(String.format("=== postProcessBeforeInitialization ===> bean.name = [%s]", beanName));
//        }
        return bean;
    }


    // 每一个bean，都会执行
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        String packageName = Optional.ofNullable(bean).map(Object::getClass).map(Class::getPackage).map(Package::getName).orElseGet(() -> "");
//        if (packageName.startsWith("com.miya")) {
//            log.info(String.format("=== postProcessAfterInitialization ===> bean.class = [%s]", bean.getClass()));
//            log.info(String.format("=== postProcessAfterInitialization ===> bean.name = [%s]", beanName));
//        }
        return bean;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("=== serverPort ===" + serverPort);
        log.info("=== InitializingBean ===");
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("=== ApplicationRunner ===");
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("=== CommandLineRunner ===");
    }

    @PostConstruct
    public void init(){
        log.info("=== PostConstruct ===");
    }
}
