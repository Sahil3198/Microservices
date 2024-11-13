package com.parekh.AuthenticationService.security;

import com.parekh.AuthenticationService.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
//
//    @Autowired
//    private JwtFilter jwtFilter;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/authenticate/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

//    @Bean
//    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//        http
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)  // Disable CSRF for API requests
//                .authorizeExchange(exchange -> exchange
//                        .pathMatchers("/authenticate/login").permitAll()  // Allow unauthenticated access to specific endpoints
//                        .anyExchange().authenticated()  // All other requests require authentication
//                )
//                .authenticationManager(reactiveAuthenticationManager())
//                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)  // Disable basic authentication
//                .formLogin(ServerHttpSecurity.FormLoginSpec::disable);  // Disable form login redirection
//
//        return http.build();
//    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

//    @Bean
//    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
//        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager =
//                new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
//        authenticationManager.setPasswordEncoder(passwordEncoder());
//        return authenticationManager;
//    }

//    @Bean
//    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
//        return new ReactiveAuthenticationManager() {
//            @Override
//            public Mono<Authentication> authenticate(Authentication authentication) {
//                String username = authentication.getName();
//                String password = authentication.getCredentials().toString();
//
//                // Fetch user details from a reactive UserDetailsService
//                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//                if (passwordEncoder().matches(password, userDetails.getPassword())) {
//                    // Create authentication token if password matches
//                    return Mono.just(new UsernamePasswordAuthenticationToken(
//                            userDetails.getUsername(),
//                            userDetails.getPassword(),
//                            userDetails.getAuthorities()));
//                } else {
//                    // Handle invalid password
//                    return Mono.error(new BadCredentialsException("Invalid password"));
//                }
//            }
//        };
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
