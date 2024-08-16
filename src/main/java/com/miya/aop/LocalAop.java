package com.miya.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LocalAop {

    @Before("execution(* com.miya.bean.BeanA.print(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("aop -> logBefore");
    }

}
