package com.hackathon.finservice.DTO.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class LoginRequest {
    private String identifier;
    private String password;
}
