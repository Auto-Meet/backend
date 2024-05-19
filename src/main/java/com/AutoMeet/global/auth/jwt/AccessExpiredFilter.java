package com.AutoMeet.global.auth.jwt;

import com.AutoMeet.global.auth.exception.AccessTokenExpiredException;
import com.AutoMeet.global.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.AutoMeet.global.exception.ErrorCode.ACCESS_TOKEN_EXPIRED;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessExpiredFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AccessTokenExpiredException e) {
            log.error("Access token expired", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            objectMapper.writeValue(response.getWriter(), new ErrorResponse(ACCESS_TOKEN_EXPIRED.getCode(), ACCESS_TOKEN_EXPIRED.getMessage()));
        }
    }
}