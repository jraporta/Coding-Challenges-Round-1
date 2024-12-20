package com.hackathon.finservice.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterResponse {
    private String name;
    private String email;
    private String accountNumber;
    private String accountType;
    private String hashedPassword;
}
