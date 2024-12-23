package com.hackathon.finservice.Controllers;

import com.hackathon.finservice.DTO.DtoMapper;
import com.hackathon.finservice.DTO.response.AccountResponse;
import com.hackathon.finservice.DTO.response.UserInfoResponse;
import com.hackathon.finservice.Entities.Account;
import com.hackathon.finservice.Exception.AccountNotFoundException;
import com.hackathon.finservice.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {

    UserService userService;
    DtoMapper mapper;

    @GetMapping("/dashboard/user")
    public ResponseEntity<UserInfoResponse> retrieveUserDetails(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(userService.retrieveUserDetails(userDetails.getUsername()));
    }

    @GetMapping("/dashboard/account")
    public ResponseEntity<AccountResponse> retrieveMainAccountDetails(@AuthenticationPrincipal UserDetails userDetails){
        Account account = userService.retrieveMainAccount(userDetails.getUsername());
        return ResponseEntity.ok(mapper.mapToAccountResponse(account));
    }

    @GetMapping("/dashboard/account/{index}")
    public ResponseEntity<AccountResponse> retrieveAccountDetails(@AuthenticationPrincipal UserDetails userDetails,
                                                                  @PathVariable Integer index){
        try {
            return ResponseEntity.ok(userService.retrieveAccountDetails(userDetails.getUsername(), index));
        } catch (IndexOutOfBoundsException e) {
            throw new AccountNotFoundException("Account not found");
        }
    }



}
