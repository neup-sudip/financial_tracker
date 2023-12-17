package com.example.financialtracker.security;

import com.example.financialtracker.wrapper.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();
        ApiResponse<String> apiResponse = new ApiResponse<>(false, "", "Unauthorized");
        response.setContentType("application/json");
        response.setStatus(401);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
