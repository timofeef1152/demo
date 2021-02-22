package com.example.demo.exceptions;

public class LoginAttemptBlockedException extends RuntimeException {
    public LoginAttemptBlockedException() {
    }

    public LoginAttemptBlockedException(String message) {
        super(message);
    }
}
