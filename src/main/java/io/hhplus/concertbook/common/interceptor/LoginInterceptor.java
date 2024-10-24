package io.hhplus.concertbook.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.naming.AuthenticationException;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 세션에서 사용자 정보를 확인
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            throw new AuthenticationException("로그인안됨.");
//            response.sendRedirect("/login");
//            return false;
        }
        log.info("로그인성공");
        return true;

// 보류 : 인터셉터에서 이렇게 json 객체 읽는건 성능상으로 부담 아닐까
//        if ("POST".equalsIgnoreCase(request.getMethod())) {
//            StringBuilder sb = new StringBuilder();
//            BufferedReader reader = request.getReader();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                sb.append(line);
//            }
//            String body = sb.toString();
//            JSONObject jsonObject = new JSONObject(body);
//            String token = jsonObject.getString("token");
//        }
        }
}