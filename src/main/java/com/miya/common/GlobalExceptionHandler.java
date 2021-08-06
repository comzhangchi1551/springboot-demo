package com.miya.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;
import java.util.StringJoiner;

/**
 * Auth: 张
 * Desc: 全局异常处理类
 * Date: 2021/2/21 15:05
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 自定义异常捕捉
     * @param e
     * @return
     */
    @ExceptionHandler(BaseException.class)
    public BaseResult baseExceptionHandle(BaseException e, ServletWebRequest request){
        log.info("应用程序已知异常[{}],请求路径[{}]", e.getMessage(), getRequestPath(request),e);
        return BaseResult.error(e.getMessage());
    }

    /**
     * 系统未知异常统一处理
     *
     * @param e       异常
     * @param request 请求
     */
    @ExceptionHandler(value = {Exception.class})
    public BaseResult<Void> exception(Exception e, ServletWebRequest request) {
        log.error("应用程序未知异常[{}],请求路径[{}]", e.getMessage(), getRequestPath(request), e);
        return BaseResult.error(e.getMessage());
    }

    /**
     * spring mvc抛出 请求方法不支持
     *
     * @param e
     * @param request
     */
    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public BaseResult<Void> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e,
                                                                  ServletWebRequest request) {
        log.info("请求方法不支持[{}],请求路径[{}]", e.getMessage(), getRequestPath(request));
        return BaseResult.error(e.getMessage());
    }


    /**
     * 文件上传过大；
     *
     * @param e
     * @param request
     */
    @ExceptionHandler(value = {MaxUploadSizeExceededException.class})
    public BaseResult<Void> maxUploadSizeExceededException(MaxUploadSizeExceededException e,
                                                                   ServletWebRequest request) {
        log.info("上传文件太大，请求路径[{}]", getRequestPath(request));
        return BaseResult.error(String.format("上传文件太大，message = %s", e.getMessage()));
    }

    /**
     * spring mvc文件上传异常
     *
     * @param e
     * @param request
     */
    @ExceptionHandler(value = {MultipartException.class})
    public BaseResult<Void> multipartException(MultipartException e, ServletWebRequest request) {
        log.info("请求方法不支持[{}],请求路径[{}]", e.getMessage(), getRequestPath(request));
        return BaseResult.error(e.getMessage());
    }

    /**
     * spring mvc抛出 请求方法不支持
     *
     * @param e
     * @param request
     */
    @ExceptionHandler(value = {HttpMediaTypeNotSupportedException.class})
    public BaseResult<Void> httpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e,
                                                              ServletWebRequest request) {
        log.info("请求MediaType不支持[{}],请求路径[{}]", e.getMessage(), getRequestPath(request));
        return BaseResult.error(e.getMessage());
    }

    /**
     * 请求缺少参数异常
     *
     * @param e       异常
     * @param request 请求
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public BaseResult<Void> missingServletRequestParameterException(MissingServletRequestParameterException e,
                                                                   ServletWebRequest request) {
        log.info("缺少RequestParam参数[{}],请求路径[{}]", e.getParameterName(), getRequestPath(request));
        return BaseResult.error(e.getMessage());
    }

    /**
     * 请求缺少参数异常
     *
     * @param e       异常
     * @param request 请求
     */
    @ExceptionHandler(value = MissingServletRequestPartException.class)
    public BaseResult<Void> missingServletRequestPartException(MissingServletRequestPartException e,
                                                              ServletWebRequest request) {
        log.info("缺少参数[{}],请求路径[{}]", e.getRequestPartName(), getRequestPath(request));
        return BaseResult.error(e.getMessage());
    }

    /**
     * @param e
     * @param request
     */
    @ExceptionHandler(value = ServletRequestBindingException.class)
    public BaseResult<Void> servletRequestBindingException(ServletRequestBindingException e, ServletWebRequest request) {
        log.info("请求路径[{}],异常:{}", getRequestPath(request), e.getMessage());
        return BaseResult.error(e.getMessage());
    }

    /**
     * 数据库异常
     *
     * @param e       异常
     * @param request 请求
     */
    @ExceptionHandler(value = {DataAccessException.class})
    public BaseResult<Void> dataAccessException(DataAccessException e, ServletWebRequest request) {
        log.error("数据库异常[{}],请求路径[{}],错误信息", e.getMessage(), getRequestPath(request), e);
        // 避免数据库表信息外泄
        return BaseResult.error(e.getMessage());
    }

    /**
     * jsr303 校验错误 (GET 请求)
     *
     * @author zhoutuo
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    public BaseResult<Void> handleResourceNotFoundException(ConstraintViolationException e, ServletWebRequest request) {
        StringBuilder msg = new StringBuilder();
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        if (!constraintViolations.isEmpty()) {
            for (ConstraintViolation<?> item : constraintViolations) {
                msg.append(item.getMessageTemplate());
                msg.append(",");
            }
            msg.deleteCharAt(msg.length() - 1);
        }
        log.info("参数校验异常[{}]，请求路径[{}]", msg, getRequestPath(request));
        return BaseResult.error(e.getMessage());
    }

    /**
     * jsr303 校验错误（处理post请求中的参数校验异常）；
     * @param exception
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value= MethodArgumentNotValidException.class)
    public BaseResult MethodArgumentNotValidHandler(MethodArgumentNotValidException exception) {
        StringJoiner stringJoiner = new StringJoiner(": ");
        // 只返回第一个错误信息即可；
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            stringJoiner.add(error.getField()).add(error.getDefaultMessage());
            break;
        }
        return BaseResult.error(stringJoiner.toString());
    }

    /**
     * 缺少request Body
     *
     * @author zhoutuo
     */
    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public BaseResult<Void> handleHttpMessageNotReadableExceptionException(HttpMessageNotReadableException e,
                                                                          ServletWebRequest request) {
        log.info("缺少requestBody:{}，请求路径[{}]", e.getMessage(), getRequestPath(request), e);
        return BaseResult.error(e.getMessage());
    }

    /**
     * 获取request的描述信息
     */
    protected String getRequestPath(ServletWebRequest request) {
        HttpServletRequest httpRequest = request.getRequest();
        StringBuffer requestURL = httpRequest.getRequestURL();
        return requestURL.toString();
    }
}
