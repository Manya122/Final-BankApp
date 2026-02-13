package com.bankapp.service;

import com.bankapp.dto.LoginRequest;
import com.bankapp.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}