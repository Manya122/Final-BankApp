package com.bankapp.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bankapp.dto.CreateClerkRequest;
import com.bankapp.dto.UserResponse;
import com.bankapp.entity.User;
import com.bankapp.entity.UserRole;
import com.bankapp.repository.UserRepository;
import com.bankapp.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    @Override
    public UserResponse createClerk(CreateClerkRequest req) {

        if (repo.findByUsername(req.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        var user = repo.save(
                User.builder()
                        .username(req.getUsername())
                        .password(encoder.encode(req.getPassword()))
                        .role(UserRole.CLERK)
                        .active(true)
                        .build()
        );

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name())
                .active(user.isActive())
                .build();
    }
}
