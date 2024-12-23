package com.hackathon.finservice.Controllers;

import com.hackathon.finservice.DTO.request.AccountCreationRequest;
import com.hackathon.finservice.Service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AccountController {

    AccountService accountService;

    @PostMapping("/account/create")
    public ResponseEntity<String> createAccount(@RequestBody AccountCreationRequest request){
        accountService.createAccount(request.getAccountNumber(), request.getAccountType());
        return ResponseEntity.ok("New account added successfully for user");
    }




}
