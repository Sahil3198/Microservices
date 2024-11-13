package com.parekh.AuthenticationService.controller;

import com.parekh.AuthenticationService.model.AuthenticationRequest;
import com.parekh.AuthenticationService.model.AuthenticationResponse;
import com.parekh.AuthenticationService.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/authenticate")
public class AuthenticationController {

    @Autowired
    private AuthenticationService service;

    @GetMapping("/hello")
    public ResponseEntity<String> helloFromAuthentication(){
        return ResponseEntity.ok("Hello!");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {

        Optional<AuthenticationResponse> authenticationResponse = service.verify(request);
        return ResponseEntity.ok(authenticationResponse.get());
    }
}
