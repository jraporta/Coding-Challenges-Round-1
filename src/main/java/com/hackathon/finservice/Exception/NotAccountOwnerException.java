package com.hackathon.finservice.Exception;

public class NotAccountOwnerException extends RuntimeException {
    public NotAccountOwnerException(String message) {
        super(message);
    }
}
