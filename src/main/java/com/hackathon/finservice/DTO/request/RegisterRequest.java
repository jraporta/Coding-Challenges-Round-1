package com.hackathon.finservice.DTO.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {

    @NotEmpty(message = "Name is required")
    private String name;

    @NotEmpty(message = "Password is required")
    private String password;

    @NotEmpty(message = "email is required")
    @Email(message = "Invalid email")
    private String email;
}
