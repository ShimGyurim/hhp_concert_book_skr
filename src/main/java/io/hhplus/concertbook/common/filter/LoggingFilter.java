package io.hhplus.concertbook.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Slf4j
//@WebFilter(urlPatterns = "/concert/date")
public class LoggingFilter implements Filter {
//    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 초기화 코드 (필요 시)
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.info("Request received: {}", request);

        //전처리 과정 - HttpServletRequest와 HttpServletResponse를 캐시 가능하도록 래핑해준다.
        ContentCachingRequestWrapper httpServletRequest = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper httpServletResponse = new ContentCachingResponseWrapper((HttpServletResponse) response);

        chain.doFilter(request, response);


        //request 요청으로 어떤 uri 가 들어왔는지 확인
        String uri = httpServletRequest.getRequestURI();
        String reqContent = new String(httpServletRequest.getContentAsByteArray());
        log.info("uri : {}, request : {}", uri, reqContent);

        int httpStatus = httpServletResponse.getStatus();
        String resContent = new String(httpServletResponse.getContentAsByteArray());

        //주의 : response를 클라이언트에서 볼 수 있도록 하려면 response를 복사해야 한다. response를 콘솔에 보여주면 내용이 사라진다.
//        httpServletResponse.copyBodyToResponse();

        log.info("status: {}, response {}", httpStatus, resContent);
    }

    @Override
    public void destroy() {
        // 정리 코드 (필요 시)
    }
}