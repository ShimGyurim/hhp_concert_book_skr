package io.hhplus.concertbook.common.filter;

import io.hhplus.concertbook.common.wrapper.RequestWrapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class XSSFilter implements Filter {
    public FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws SecurityException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void destroy(){
        this.filterConfig = null;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("xss filter");
        chain.doFilter(new RequestWrapper((HttpServletRequest) request), response);
    }
}
