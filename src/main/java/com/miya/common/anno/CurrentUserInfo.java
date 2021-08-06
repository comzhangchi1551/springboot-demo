package com.miya.common.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @Auth: 张
 *
 * @Desc： 自定义注解，用于表示需要自动注入数据的参数；
 * @Date： 2021/3/1
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUserInfo {
}
