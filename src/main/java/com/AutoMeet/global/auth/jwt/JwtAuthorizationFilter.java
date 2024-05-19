package com.AutoMeet.global.auth.jwt;

import com.AutoMeet.domain.user.model.User;
import com.AutoMeet.domain.user.repository.UserRepository;
import com.AutoMeet.global.auth.PrincipalDetails;
import com.AutoMeet.global.auth.exception.AccessTokenExpiredException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;


public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;
    private final JwtProperties jwtProperties;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, JwtProperties jwtProperties) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.jwtProperties = jwtProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String header = request.getHeader(jwtProperties.getHeaderString());

        if (header == null || !header.startsWith(jwtProperties.getTokenPrefix())) {
            chain.doFilter(request, response);
            return;
        }

        String token = request.getHeader(jwtProperties.getHeaderString())
                .replace(jwtProperties.getTokenPrefix(), "");

        try {
            String email = JWT.require(Algorithm.HMAC512(jwtProperties.getSecret())).build().verify(token)
                    .getClaim("email").asString();

            if (email != null) {
                User userEntity = userRepository.findByEmail(email);

                PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        principalDetails,
                        null,
                        principalDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (TokenExpiredException e) {
            throw new AccessTokenExpiredException("Access 토큰 만료");
        }
        chain.doFilter(request, response);
    }
}
