package com.example.demo.exceptions;

public class CurrencyDoesntMatchException extends RuntimeException {
    public CurrencyDoesntMatchException() {
    }

    public CurrencyDoesntMatchException(String message) {
        super(message);
    }
}
