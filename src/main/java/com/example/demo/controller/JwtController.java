package com.example.demo.controller;

import com.example.demo.model.dto.JwtRequest;
import com.example.demo.model.dto.JwtResponse;
import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token, HttpServletRequest request) {
        request.getSession().invalidate();
        SecurityContextHolder.getContext().setAuthentication(null);
        authService.disableAuthenticationToken(token, request.getRemoteAddr());
        return ResponseEntity.ok("Bye, have a beautiful time!");
    }
}