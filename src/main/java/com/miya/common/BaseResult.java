package com.miya.common;

import lombok.Data;

/**
 * Auth: 张
 * Desc: 自定义统一返回体
 * Date: 2021/2/21 15:07
 */
@Data
public class BaseResult<T> {
    private String msg;
    private Integer code;
    private T data;


    public BaseResult(String msg, Integer code, T data) {
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public static BaseResult success(){
        return new BaseResult("success", 200, null);
    }

    public static BaseResult success(Object data){
        return new BaseResult("success", 200, data);
    }

    public static BaseResult error(){
        return new BaseResult("内部错误", -1, null);
    }

    public static BaseResult error(String msg){
        return new BaseResult(msg, -1, null);
    }
}
