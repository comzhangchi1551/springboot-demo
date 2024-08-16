package com.miya.bean;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
@Slf4j
public class BeanAImpl implements BeanA {

    @Autowired
    private BeanBImpl beanB;

    @Override
    public void print() {
        log.info("BeanAImpl print!");
    }
}
