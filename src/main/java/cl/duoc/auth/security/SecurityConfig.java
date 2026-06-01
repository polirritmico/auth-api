/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.auth.security;

import cl.duoc.auth.exception.ForbiddenAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Value("${jwt.public}")
    private String[] publicPaths;

    private final String publicAuthEndpoint = "/api/v1/auth";
    private final String publicRegisterEndpoint = "/api/v1/auth/register";
    private final String changeOwnPasswordEndpoint = publicAuthEndpoint + "/me/password";
    private final String allowedRole = "admin";

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST, publicAuthEndpoint)
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, publicRegisterEndpoint)
                        .permitAll()
                        .requestMatchers(publicPaths)
                        .permitAll()
                        .requestMatchers(HttpMethod.PUT, changeOwnPasswordEndpoint)
                        .authenticated()
                        .anyRequest()
                        .hasAuthority(allowedRole))
                .exceptionHandling(ex -> ex.accessDeniedHandler((req, res, accessDeniedException) ->
                        handlerExceptionResolver.resolveException(req, res, null, new ForbiddenAccessException())))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
