package com.parekh.AuthenticationService.service;

import com.parekh.AuthenticationService.model.AuthenticationRequest;
import com.parekh.AuthenticationService.model.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authManager;

    //private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Optional<AuthenticationResponse> verify(AuthenticationRequest authenticationRequest) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return Optional.of(new AuthenticationResponse(jwtService.generateToken(authenticationRequest.getUsername())));
        } else {
            return Optional.empty();
        }
    }
}
