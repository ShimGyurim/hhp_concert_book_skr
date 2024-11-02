package io.hhplus.concertbook.common.config;

import io.hhplus.concertbook.common.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/concert/book","/concert/payments","/auth/token") // 모든 경로에 대해 인터셉터 적용
                .excludePathPatterns("/login"); // 로그인 및 회원가입 경로는 제외
    }
}