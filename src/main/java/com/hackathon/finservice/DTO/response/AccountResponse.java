package com.hackathon.finservice.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountResponse {
    private String accountNumber;
    private double balance;
    private String accountType;
}
