package com.AutoMeet.global.auth.config;

import com.AutoMeet.domain.user.repository.UserRepository;
import com.AutoMeet.global.auth.PrincipalDetailsService;
import com.AutoMeet.global.auth.jwt.AccessExpiredFilter;
import com.AutoMeet.global.auth.jwt.JwtAuthenticationFilter;
import com.AutoMeet.global.auth.jwt.JwtAuthorizationFilter;
import com.AutoMeet.global.auth.jwt.JwtProperties;
import com.AutoMeet.global.config.CorsConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfig corsConfig;
    private final UserRepository userRepository;
    private final PrincipalDetailsService userDetailsService;
    private final AccessExpiredFilter accessExpiredFilter;
    private final JwtProperties jwtProperties;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.formLogin(formLogin -> formLogin.disable());

        AuthenticationManagerBuilder sharedObject = http.getSharedObject(AuthenticationManagerBuilder.class);

        sharedObject.userDetailsService(this.userDetailsService);
        AuthenticationManager authenticationManager = sharedObject.build();

        http.authenticationManager(authenticationManager);

        http.httpBasic(httpBasic ->
                httpBasic.disable()
        );

        http.addFilterBefore(corsConfig.corsFilter(), SecurityContextPersistenceFilter.class);
        http.addFilterBefore(new JwtAuthenticationFilter(authenticationManager, jwtProperties), UsernamePasswordAuthenticationFilter.class);
        http.addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository, jwtProperties));
        http.addFilterBefore(accessExpiredFilter, JwtAuthorizationFilter.class);

        http.authorizeHttpRequests(authorize ->
                authorize
                        .requestMatchers("/api/user/**").permitAll()
                        .requestMatchers("/api/token/**").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .requestMatchers("/api/answer/**").hasAnyRole("ADMIN")

                        .anyRequest().permitAll()
        );

        return http.build();
    }
}