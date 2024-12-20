package com.hackathon.finservice.DTO.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterRequest {
    @NotEmpty private String name;
    @NotEmpty private String password;
    @NotEmpty private String email;
}
