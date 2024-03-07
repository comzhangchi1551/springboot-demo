package com.miya.conf;

import com.miya.common.anno.CurrentUserInfoResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
public class MvcConf extends WebMvcConfigurationSupport {

    @Autowired
    private CurrentUserInfoResolver currentUserInfoResolver;

    /**
     * 参数解析器
     *
     * @param argumentResolvers
     */
    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(currentUserInfoResolver);
        super.addArgumentResolvers(argumentResolvers);
    }

}
