package com.parekh.ApiGateway.service;

import com.parekh.ApiGateway.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RefreshScope
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JWTUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            System.out.println(request.getURI().getPath());
            if (validator.isSecured.test(request)) {
                if (authMissing(request)) {
                    return onError(exchange);
                }

                String token = request.getHeaders().getOrEmpty("Authorization").get(0);
                if (token != null && token.startsWith("Bearer ")) {
                    token = token.substring(7);
                }

                try {
                    jwtUtil.validateToken(token);
                } catch (Exception e) {
                    return onError(exchange);
                }
            }
            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        System.out.println("Error");
        return response.setComplete();
    }

    private boolean authMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    // Inner config class (can be empty if no specific config is required)
    public static class Config {
        // Define configuration properties here if needed
    }
}
