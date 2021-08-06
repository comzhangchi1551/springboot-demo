package com.miya.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Auth: 张
 * Desc: 过滤器模板（过滤器可以直接使用spring容器中的类，比拦截器来的友好）
 * Date: 2021/2/22 20:25
 */

@Slf4j
@Component
@WebFilter(urlPatterns = "/**", filterName = "TempFilter")
public class TempFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.error("Filter ===> init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.error("<<<<<<<<< doFilter");
        filterChain.doFilter(servletRequest, servletResponse);
        log.error("doFilter >>>>>>>>");
    }

    @Override
    public void destroy() {
        log.error("Filter ===> destroy");
    }
}
