package com.bankapp.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankapp.dto.CreateClerkRequest;
import com.bankapp.dto.UserResponse;
import com.bankapp.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/clerks")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public UserResponse createClerk(
            @RequestBody CreateClerkRequest request) {

        return userService.createClerk(request);
    }
}
