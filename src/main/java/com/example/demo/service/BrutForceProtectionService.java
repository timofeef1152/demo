package com.example.demo.service;

import com.example.demo.model.entity.LoginAttemptInfo;
import com.example.demo.model.entity.User;
import com.example.demo.repository.LoginAttemptInfoRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class BrutForceProtectionService {
    private final int ATTEMPTS_AVAILABLE_COUNT = 5;
    private final int BAN_SECONDS = 300;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginAttemptInfoRepository loginAttemptInfoRepository;

    @Transactional
    public void protect(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (!optionalUser.isPresent()) {
            return;
        }
        User user = optionalUser.get();
        LoginAttemptInfo loginAttemptInfo = user.getLoginAttemptInfo();
        Integer attemptQuantity = loginAttemptInfo.getAttemptQuantity();
        if (attemptQuantity > ATTEMPTS_AVAILABLE_COUNT) {
            LocalDateTime accessibleLoginDatetime = LocalDateTime.now().plus(BAN_SECONDS, ChronoUnit.SECONDS);
            loginAttemptInfo.setAccessibleLoginDateTime(accessibleLoginDatetime);
            sendLoginFailedEmailNotification(user);
        }
        Integer updatedAttemptQuantity = attemptQuantity + 1;
        loginAttemptInfo.setAttemptQuantity(updatedAttemptQuantity);
        loginAttemptInfoRepository.save(loginAttemptInfo);
    }

    @Transactional
    public void resetLoginAttempts(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        optionalUser.ifPresent(user -> {
            LoginAttemptInfo loginAttemptInfo = user.getLoginAttemptInfo();
            loginAttemptInfo.setAttemptQuantity(0);
            loginAttemptInfoRepository.save(loginAttemptInfo);
            sendLoginSuccessEmailNotification(user);
        });
    }

    private void sendLoginFailedEmailNotification(User user) {
        //TODO notify the user about the blocked login
    }

    private void sendLoginSuccessEmailNotification(User user) {
        //TODO notify the user about the logging in
    }
}
