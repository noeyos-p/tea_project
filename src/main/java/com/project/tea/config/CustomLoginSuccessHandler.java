package com.project.tea.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        String userEmail = authentication.getName(); // 로그인한 사용자 이메일

        if ("admin@admin.com".equalsIgnoreCase(userEmail)) {
            response.sendRedirect("/admin");
        } else {
            response.sendRedirect("/choice");
        }
    }
}

