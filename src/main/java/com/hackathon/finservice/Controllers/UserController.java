package com.hackathon.finservice.Controllers;

import com.hackathon.finservice.DTO.response.UserInfoResponse;
import com.hackathon.finservice.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {

    UserService userService;

    @GetMapping("/dashboard/user")
    public ResponseEntity<UserInfoResponse> retrieveUserDetails(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(userService.retrieveUserDetails(userDetails.getUsername()));
    }



}
