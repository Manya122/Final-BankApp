package com.bankapp.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.bankapp.dto.LoginRequest;
import com.bankapp.dto.LoginResponse;
import com.bankapp.repository.UserRepository;
import com.bankapp.security.JwtService;
import com.bankapp.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public LoginResponse login(LoginRequest req) {

        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                req.getUsername(),
                req.getPassword()
            )
        );

        var user = userRepository
            .findByUsername(req.getUsername())
            .orElseThrow();

        String token = jwtService.generateToken(
            user.getUsername(),
            user.getRole().name(),
            user.getId()
        );

        return LoginResponse.builder()
                .token(token)
                .role(user.getRole().name())
                .username(user.getUsername())
                .build();
    }
}
