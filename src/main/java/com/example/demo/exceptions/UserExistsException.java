package com.example.demo.exceptions;

public class UserExistsException extends RuntimeException {
    public UserExistsException() {
    }

    public UserExistsException(String message) {
        super(message);
    }
}
