package com.miya.common.anno;

import com.miya.entity.cost.SeckillLoginCost;
import com.miya.entity.model.SUser;
import com.miya.service.SeckillUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Auth: 张
 * Desc: 将使用了@CurrentUserInfo注解标注的controller层的TempUser参数，填充属性；
 * Date: 2021/3/1 17:36
 */
@Component
public class CurrentUserInfoResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private SeckillUserService seckillUserService;

    /**
     * 校验怎样的参数才进入解析方法；
     * 参数类型为TempUser，并且添加了CurrentUserInfo注解的参数才进行解析；
     * @param parameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(SUser.class)
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
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        String cookieToken = getCookieValue(request, SeckillLoginCost.LOGIN_COOKIE_NAME);
        SUser user = seckillUserService.selectSeckillUserFromRedis(cookieToken);
        return user;
    }

    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
