package com.hackathon.finservice.Controllers;

import com.hackathon.finservice.DTO.request.LoginRequest;
import com.hackathon.finservice.DTO.request.RegisterRequest;
import com.hackathon.finservice.DTO.response.LoginResponse;
import com.hackathon.finservice.DTO.response.UserInfoResponse;
import com.hackathon.finservice.Service.AuthenticationService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<UserInfoResponse> register(@Validated @RequestBody RegisterRequest request) {
        log.debug("Register request with data: {}", request);
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        log.debug("Login request with data: {}", request);
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        log.debug("Logout request");
        authenticationService.logout(token);
        return ResponseEntity.ok("");
    }

}

