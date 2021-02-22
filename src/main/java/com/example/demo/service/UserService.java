package com.example.demo.service;

import com.example.demo.model.dto.JwtRequest;
import com.example.demo.model.entity.Account;
import com.example.demo.model.entity.LoginAttemptInfo;
import com.example.demo.model.entity.User;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.LoginAttemptInfoRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.exceptions.UserExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoginAttemptInfoRepository loginAttemptInfoRepository;

    @Transactional
    public User create(JwtRequest userDto) throws UserExistsException {
        if (existsWithUsername(userDto.getUsername())) {
            throw new UserExistsException("User with username:\t" + userDto.getUsername() + " already exists!");
        }
        User user = new User();

        LoginAttemptInfo loginAttemptInfo = new LoginAttemptInfo();
        loginAttemptInfo.setUser(user);
        loginAttemptInfo.setAttemptQuantity(0);
        loginAttemptInfo.setAccessibleLoginDateTime(LocalDateTime.now());
//        loginAttemptInfo = loginAttemptInfoRepository.save(loginAttemptInfo);

        user.setLoginAttemptInfo(loginAttemptInfo);
        user.setAccounts(new ArrayList<>());
        user.setUsername(userDto.getUsername());
        user.setPasswordHash(encoder.encode(userDto.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public User addAccounts(User user, List<Account> accounts) {
        accounts.forEach(account -> {
            user.getAccounts().add(account);
            account.setUser(user);
            accountRepository.save(account);
        });
        return userRepository.save(user);
    }

    @Transactional
    public boolean existsWithUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}