package com.github.ecommerce.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;


// AuthenticationEntryPoint는 인증 과정에서 실패했을 때, 즉 사용자가 인증되지 않은 상태에서 보호된 자원에 접근하려고 할 때 실행되는 컴포넌트
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendRedirect("/exceptions/entrypoint"); // `/exceptions/entrypoint`는 ExceptionController의 URI이다.
        // 인증 실패 시 클라이언트에게 HTTP 401 Unauthorized 상태 코드와 함께 JSON 형태의 에러 메시지를 반환
//        response.setContentType("application/json;charset=UTF-8");
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.getWriter().write("{\"error\": \"Unauthorized\"}");
    }
}
