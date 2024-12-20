package com.hackathon.finservice.Service;

import com.hackathon.finservice.DTO.request.LoginRequest;
import com.hackathon.finservice.DTO.request.RegisterRequest;
import com.hackathon.finservice.DTO.response.LoginResponse;
import com.hackathon.finservice.DTO.response.RegisterResponse;
import com.hackathon.finservice.Entities.Account;
import lombok.AllArgsConstructor;
import com.hackathon.finservice.Entities.User;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final AuthenticationServiceHelper helper;
    private final UserService userService;
    private final AccountService accountService;

    public RegisterResponse register(RegisterRequest request) {
        helper.checkEmailFormat(request.getEmail());
        helper.checkPasswordFormat(request.getPassword());
        //checkEmailExists
        User user = userService.createUser(request.getName(), request.getEmail(), request.getPassword());
        Account account = accountService.createAccount(user.getId());
        return helper.mapToRegisterResponse(user, account);
    }

    //TODO
    public LoginResponse login(LoginRequest request) {
        return new LoginResponse("");
    }

    //TODO
    public String logout() {
        return "";
    }
}
