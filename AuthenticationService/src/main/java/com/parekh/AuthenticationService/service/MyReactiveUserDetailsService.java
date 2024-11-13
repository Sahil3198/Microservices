package com.parekh.AuthenticationService.service;

import com.parekh.AuthenticationService.client.UserServiceClient;
import com.parekh.AuthenticationService.model.MyUserDetails;
import com.parekh.AuthenticationService.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

//@Service
//public class MyReactiveUserDetailsService implements ReactiveUserDetailsService {
//
//    @Autowired
//    private UserServiceClient userServiceClient;
//
//    @Override
//    public Mono<UserDetails> findByUsername(String username) {
//        ResponseEntity<MyUserDetails> userDetailsResponseEntity = userServiceClient.getUserDetails(username);
//        MyUserDetails userDetails = userDetailsResponseEntity.getBody();
//        if (userDetails == null) {
//            throw new UsernameNotFoundException("user not found");
//        }
//        return Mono.just(new UserPrincipal(userDetails));
//    }
//}
