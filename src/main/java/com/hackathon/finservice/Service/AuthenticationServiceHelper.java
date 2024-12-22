package com.hackathon.finservice.Service;

import com.hackathon.finservice.DTO.response.RegisterResponse;
import com.hackathon.finservice.Entities.Account;
import com.hackathon.finservice.Entities.User;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationServiceHelper {

    //TODO
    public void checkEmailFormat(@NotEmpty String email) {
    }

    //TODO
    public void checkPasswordFormat(@NotEmpty String password) {
    }

    public RegisterResponse mapToRegisterResponse(User user, Account account) {
        return new RegisterResponse(
                user.getName(),
                user.getEmail(),
                account.getAccountNumber(),
                account.getAccountType(),
                user.getHashedPassword()
        );
    }
}
