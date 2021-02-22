package com.example.demo.controller;

import com.example.demo.model.dto.JwtRequest;
import com.example.demo.model.dto.JwtResponse;
import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping(
        value = "/user",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class JwtController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/login")
    public ResponseEntity<?> auth(@RequestBody JwtRequest authRequest, HttpServletRequest request) {
        final String token = authService.auth(
                authRequest.getUsername(),
                authRequest.getPassword(),
                request.getRemoteAddr());
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        authService.disableAuthenticationToken(token);
        return ResponseEntity.ok("Bye, have a beautiful time!");
    }
}