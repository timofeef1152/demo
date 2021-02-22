package com.example.demo.controller;

import com.example.demo.model.dto.AccountPaymentDto;
import com.example.demo.service.AccountService;
import com.example.demo.exceptions.AccountPermissionDeniedException;
import com.example.demo.exceptions.CurrencyDoesntMatchException;
import com.example.demo.exceptions.InsufficientFundsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@CrossOrigin
@RequestMapping(
        value = "",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/payment")
    public ResponseEntity<String> doPayment(@RequestBody AccountPaymentDto accountPayment) {
        try {
            accountService.payment(accountPayment);
        } catch (AccountPermissionDeniedException ex) {
            return ResponseEntity.status(FORBIDDEN).body(ex.getMessage());
        } catch (CurrencyDoesntMatchException | InsufficientFundsException ex) {
            return ResponseEntity.unprocessableEntity().body(ex.getMessage());
        }
        return ResponseEntity.accepted().body("operation completed");
    }
}