package com.miya.common.anno;

import com.miya.dao.mysql.TempUserDAO;
import com.miya.entity.model.mysql.TempUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Auth: 张
 * Desc: 将使用了@CurrentUserInfo注解标注的controller层的TempUser参数，填充属性；
 * Date: 2021/3/1 17:36
 */
@Component
public class CurrentUserInfoResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private TempUserDAO tempUserDAO;

    /**
     * 校验怎样的参数才进入解析方法；
     * 参数类型为TempUser，并且添加了CurrentUserInfo注解的参数才进行解析；
     * @param parameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(TempUser.class)
                && parameter.hasParameterAnnotation(CurrentUserInfo.class);
    }


    /**
     * 解析方法，将需要解析的方法填充值；
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        TempUser tempUser = tempUserDAO.selectById(1);
        return tempUser;
    }
}
