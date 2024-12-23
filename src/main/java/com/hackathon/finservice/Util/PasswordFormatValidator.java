package com.hackathon.finservice.Util;

import com.hackathon.finservice.Exception.UserDataFormatException;
import org.springframework.stereotype.Component;

@Component
public class PasswordFormatValidator {

    private String password;

    public void setPassword(String password) {
        this.password = password;
    }

    public void validate() throws UserDataFormatException {
        if (!password.matches("^.*[A-Z]+.*$")) {
            throw new UserDataFormatException("Password must contain at least one uppercase letter");
        }
        if (!password.matches("^.*\\d.*$") && !password.matches("^.*[^\\p{Alnum}].*$")) {
            throw new UserDataFormatException("Password must contain at least one digit and one special character");
        }
        if (!password.matches("^.*\\d.*$")) {
            throw new UserDataFormatException("Password must contain at least one digit");
        }
        if (!password.matches("^.*[^\\p{Alnum}].*$")) {
            throw new UserDataFormatException("Password must contain at least one special character");
        }
        if (password.matches(".*\\s.*")) {
            throw new UserDataFormatException("Password cannot contain whitespace");
        }
        if (password.length() < 8) {
            throw new UserDataFormatException("Password must be at least 8 characters long");
        }
        if (password.length() > 127) {
            throw new UserDataFormatException("Password must be less than 128 characters long");
        }
    }

}
