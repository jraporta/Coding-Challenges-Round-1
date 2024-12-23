package com.hackathon.finservice.DTO.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RegisterRequest {

    @NotEmpty(message = "Name is required")
    private String name;

    @NotEmpty(message = "Password is required")
    private String password;

    @NotEmpty(message = "email is required")
    private String email;
}
