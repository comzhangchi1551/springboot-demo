package com.miya.common;

import com.miya.common.utils.tracer.TraceThreadLocal;
import com.miya.common.utils.tracer.TracerEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
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
//        log.error("Filter ===> init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        log.error("<<<<<<<<< doFilter");
        TracerEntry tracerEntry = TraceThreadLocal.get();
        if (tracerEntry == null) {
            TracerEntry acquire = TracerEntry.acquire();
            acquire.setPath(((HttpServletRequest) servletRequest).getRequestURI());
            TraceThreadLocal.set(acquire);
        }
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            log.error("filter过滤链出现错误", e);
        } finally {
            TraceThreadLocal.remove();
        }
//        log.error("doFilter >>>>>>>>");
    }

    @Override
    public void destroy() {
//        log.error("Filter ===> destroy");
    }
}
