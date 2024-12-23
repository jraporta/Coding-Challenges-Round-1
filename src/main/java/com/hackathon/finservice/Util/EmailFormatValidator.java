package com.hackathon.finservice.Util;

import com.hackathon.finservice.Exception.UserDataFormatException;
import org.springframework.stereotype.Component;

@Component
public class EmailFormatValidator {

    public static void validate(String email) throws UserDataFormatException {

        if (!email.matches("^[\\w-.]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
            throw new UserDataFormatException("Invalid email: " + email);
        }

    }

}
