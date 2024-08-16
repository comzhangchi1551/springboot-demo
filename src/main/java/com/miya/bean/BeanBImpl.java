package com.miya.bean;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
public class BeanBImpl implements BeanB {

    @Autowired
    private BeanCImpl beanC;

}
