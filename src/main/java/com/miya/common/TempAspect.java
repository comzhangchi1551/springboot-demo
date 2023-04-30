package com.miya.common;


import com.alibaba.excel.util.DateUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.miya.common.utils.tracer.TraceThreadLocal;
import com.miya.common.utils.tracer.TracerEntry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Auth: 张
 * Desc: 切面模板代码，请求日志打印；
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
            String result;
            if (res == null) {
                result = "";
            } else if (res instanceof String) {
                result = ((String) res).replaceAll("[\\t\\n\\r]", "-");
            } else if (res instanceof Exception) {
                Exception e = (Exception) res;
                result = JSON.toJSONString(e.getMessage());
            } else {
                result = "success";
            }
            log.info("请求路径[{}]--入参[{}]--耗时[{}]--返回结果[{}]",
                    path, splitTooLongResult(argsDesc(pjp)), tracerEntry == null ? "" : tracerEntry.release(), splitTooLongResult(result));
        }
        return res;
    }


    /**
     * 限制返回信息长度，暂设为400；
     * @param result
     * @return
     */
    private String splitTooLongResult(String result) {
        return StringUtils.abbreviate(result, 400);
    }


    /**
     * 解析入参名称及对应的数值（HttpServletRequest、HttpServletResponse、MultipartFile、BindingResult等类型不作处理）
     * @param pjp
     * @return
     */
    private String argsDesc(ProceedingJoinPoint pjp) {
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] parNames = methodSignature.getParameterNames();
        JSONObject json = new JSONObject();
        Object[] pars = pjp.getArgs();
        for (int i = 0; i < parNames.length; i++) {
            Object par = pars[i];
            if (par instanceof HttpServletRequest || par instanceof HttpServletResponse
                    || par instanceof MultipartFile || par instanceof BindingResult) {
                continue;
            }
            if (par instanceof Date) {
                json.put(parNames[i], DateUtils.format((Date) par));
            } else {
                json.put(parNames[i], par);
            }
        }
        return json.toString();
    }

}
