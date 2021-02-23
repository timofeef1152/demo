package com.example.demo.service;

import com.example.demo.model.entity.DisabledToken;
import com.example.demo.model.entity.LoginAttemptInfo;
import com.example.demo.model.entity.User;
import com.example.demo.repository.DisabledTokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtTokenUtil;
import com.example.demo.security.UserPrincipal;
import com.example.demo.exceptions.LoginAttemptBlockedException;
import com.example.demo.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuthService {

    private static final String INVALID_CREDENTIALS = "INVALID_CREDENTIALS";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DisabledTokenRepository disabledTokenRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Transactional(noRollbackFor = AuthenticationException.class)
    public String auth(String username, String password, String ipAddress) throws UsernameNotFoundException, DisabledException, BadCredentialsException {
        checkLoginAttemptAccessibility(username);

        authenticate(username, password);
        UserDetails userDetails = userRepository.findByUsername(username).map(UserPrincipal::new)
                .orElseThrow(() -> new UserNotFoundException("user " + username + " doesn't exists"));
        return jwtTokenUtil.generateToken(userDetails);
    }

    @Transactional
    public void disableAuthenticationToken(String rawToken, String ipAddress) {
        String token = jwtTokenUtil.removeBearer(rawToken);

        DisabledToken disabledToken = new DisabledToken();
        disabledToken.setToken(token);
        disabledToken.setIpAddress(ipAddress);
        disabledTokenRepository.save(disabledToken);
    }

    @Transactional
    public boolean isTokenActive(String token, UserDetails userDetails, String ipAddress) {
        Optional<DisabledToken> optionalDisabledToken = disabledTokenRepository.findByTokenAndIpAddress(token, ipAddress);
        if (optionalDisabledToken.isPresent()) {
            return false;
        }

        return jwtTokenUtil.validateToken(token, userDetails);
    }

    private void checkLoginAttemptAccessibility(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (!optionalUser.isPresent()) {
            return;
        }
        LoginAttemptInfo loginAttemptInfo = optionalUser.get().getLoginAttemptInfo();
        if (LocalDateTime.now().compareTo(loginAttemptInfo.getAccessibleLoginDateTime()) < 0) {
            throw new LoginAttemptBlockedException("blocking expiration time: " + loginAttemptInfo.getAccessibleLoginDateTime());
        }
    }

    private void authenticate(String username, String password) throws DisabledException, BadCredentialsException {
        if (Objects.isNull(username) || Objects.isNull(password)) {
            throw new BadCredentialsException(INVALID_CREDENTIALS);
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}