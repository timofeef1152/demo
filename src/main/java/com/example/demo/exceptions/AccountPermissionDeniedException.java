package com.example.demo.exceptions;

public class AccountPermissionDeniedException extends RuntimeException {
    public AccountPermissionDeniedException() {
    }

    public AccountPermissionDeniedException(String message) {
        super(message);
    }
}
