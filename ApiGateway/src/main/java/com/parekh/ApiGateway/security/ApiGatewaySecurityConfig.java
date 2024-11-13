package com.parekh.ApiGateway.security;

import com.parekh.ApiGateway.service.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.session.WebSessionManager;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class ApiGatewaySecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)  // Disable CSRF for API requests
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/authenticate/**", "/users/**", "/userServiceFallback", "/authServiceFallback").permitAll()  // Allow unauthenticated access to specific endpoints
                        .anyExchange().authenticated()  // All other requests require authentication
                )
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)  // Disable basic authentication
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable);  // Disable form login redirection

        return http.build();
    }

    @Bean
    public WebSessionManager webSessionManager() {
        return exchange -> Mono.empty();
    }
}
