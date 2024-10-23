package io.hhplus.concertbook.presentation.controller;

import io.hhplus.concertbook.domain.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    LoginService loginService;

    @PostMapping("/login")
    public Map<String, String> login(HttpServletRequest request, @RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
//        String password = credentials.get("password");
        log.info("userid: {} ",username);
        // 사용자 인증 로직 (예: 데이터베이스 조회)


        if (loginService.login(username)) {
            log.info("로그인성공");
            request.getSession().setAttribute("user", username);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful");
            return response;
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "Invalid credentials");
        return response;
    }

    @PostMapping("/logout")
    public Map<String, String> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logout successful");
        return response;
    }
}
