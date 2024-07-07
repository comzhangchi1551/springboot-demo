package com.miya.common;


import com.alibaba.excel.util.DateUtils;
import com.miya.common.utils.CJsonUtils;
import com.miya.common.utils.tracer.TraceThreadLocal;
import com.miya.common.utils.tracer.TracerEntry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Auth: 张
 * Desc: 切面模板代码，请求日志打印；
 * Date: 2021/3/15 15:25
 */
@Component
@Aspect
@Slf4j
public class TestAopAspect {

    @Pointcut("execution(* com.miya.service.TempUserService.testAop())")
    public void pointcut(){}


    @Before("pointcut()")
    public void before(JoinPoint joinPoint){
        System.out.println("before");
    }

}
