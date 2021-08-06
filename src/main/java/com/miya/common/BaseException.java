package com.miya.common;

/**
 * Auth: 张
 * Desc: 自定义异常
 * Date: 2021/2/21 15:23
 */
public class BaseException extends RuntimeException {

    public BaseException(String message) {
        super(message);
    }
}
