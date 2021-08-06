package com.miya.common;


import com.alibaba.excel.util.DateUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.miya.common.utils.tracer.TraceThreadLocal;
import com.miya.common.utils.tracer.TracerEntry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * Auth: 张
 * Desc: 切面模板代码
 * Date: 2021/3/15 15:25
 */
@Component
@Aspect
@Slf4j
public class TempAspect {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void pointcut(){}


    /**
     * @description 环绕通知
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        TracerEntry tracerEntry = TraceThreadLocal.get();
        String path = "";
        if (tracerEntry != null) {
            path = tracerEntry.getPath();
        }
        log.info("请求路径[{}],入参{}", path, splitTooLongResult(argsDesc(pjp)));
        Object res = null;
        try {
            res = pjp.proceed();
        } catch (Exception e) {
            // 记录错误的堆栈信息
            res = e;
            // 扔出异常,交给ControllerExceptionAdvice捕获
            throw e;
        } finally {
            String result = "";
            if (res == null) {
                result = "";
            } else if (res instanceof String) {
                result = ((String) res).replaceAll("[\\t\\n\\r]", "-");
            } else if (res instanceof Exception) {
                Exception e = (Exception) res;
                result = JSON.toJSONString(e.getMessage());
            } else {
                result = JSON.toJSONString(res);
            }
            log.info("请求路径[{}]--耗时[{}]--返回结果[{}]",
                    path, tracerEntry == null ? "" : tracerEntry.release(), splitTooLongResult(result));
        }
        return res;
    }

    private String splitTooLongResult(String result) {
        return StringUtils.abbreviate(result, 400);
    }


    private String argsDesc(ProceedingJoinPoint pjp) {
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] parNames = methodSignature.getParameterNames();
        JSONObject json = new JSONObject();
        Object[] pars = pjp.getArgs();
        for (int i = 0; i < parNames.length; i++) {
            if (pars[i] instanceof HttpServletRequest || pars[i] instanceof HttpServletResponse
                    || pars[i] instanceof MultipartFile || pars[i] instanceof BindingResult) {
                continue;
            }
            if (pars[i] instanceof Date) {
                json.put(parNames[i], DateUtils.format((Date) pars[i]));
            } else {
                json.put(parNames[i], pars[i]);
            }
        }
        return json.toString();
    }

}
