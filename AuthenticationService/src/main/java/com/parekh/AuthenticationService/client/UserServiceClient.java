package com.parekh.AuthenticationService.client;

import com.parekh.AuthenticationService.model.MyUserDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "UserService") // Replace with actual service URL or use service discovery
public interface UserServiceClient {

    @GetMapping("/users/details")
    ResponseEntity<MyUserDetails> getUserDetails(@RequestParam("username") String username);
}
