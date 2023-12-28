package com.example.financialtracker.security;


import com.example.financialtracker.exception.CustomException;
import com.example.financialtracker.user.User;
import com.example.financialtracker.wrapper.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final CustomUserDetailService customUserDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = jwtService.extractToken(request, "auth");
            if (!token.isEmpty()) {
                String hasConfirm = jwtService.extractToken(request, "confirm");
                jwtService.validateToken(token);
                Claims userClaim = jwtService.decodeToken(token);
                Map<String, Object> userMap = userClaim.get("user", Map.class);

                User user = new User();
                user.setUserId((Integer) userMap.get("userId"));
                user.setUsername((String) userMap.get("username"));

                request.setAttribute("user", user);

                request.setAttribute("confirm", !hasConfirm.isEmpty());

                UserDetails userDetails = customUserDetailService.loadUserByUsername(user.getUsername());

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            filterChain.doFilter(request, response);
        } catch (CustomException exception) {
            ObjectMapper objectMapper = new ObjectMapper();
            ApiResponse<String> apiResponse = new ApiResponse<>(false, null, exception.getMessage());
            response.setContentType("application/json");
            response.setStatus(exception.getStatus() | 403);
            response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        }

    }
}
