package com.hackathon.finservice.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.finservice.DTO.request.LoginRequest;
import com.hackathon.finservice.DTO.request.RegisterRequest;
import com.hackathon.finservice.DTO.response.LoginResponse;
import com.hackathon.finservice.DTO.response.RegisterResponse;
import com.hackathon.finservice.Service.AuthenticationService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final ObjectMapper objectMapper;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Validated @RequestBody RegisterRequest request) {
        log.debug("Register request with data: {}", request);
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        log.debug("Login request with data: {}", request);
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        log.debug("Logout request.");
        return ResponseEntity.ok(authenticationService.logout());
    }

}

