package com.hackathon.finservice.Controllers;

import com.hackathon.finservice.DTO.request.AccountCreationRequest;
import com.hackathon.finservice.Service.AccountService;
import com.hackathon.finservice.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AccountController {

    AccountService accountService;
    UserService userService;

    @PostMapping("/account/create")
    public ResponseEntity<String> createAccount(@RequestBody AccountCreationRequest request,
                                                @AuthenticationPrincipal UserDetails userDetails){
        Long userId = userService.checkOwnership(request.getAccountNumber(), userDetails.getUsername());
        accountService.createAccount(userId, request.getAccountType());
        return ResponseEntity.ok("New account added successfully for user");
    }




}
