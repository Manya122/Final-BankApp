package com.bankapp.service;

import com.bankapp.dto.CreateClerkRequest;
import com.bankapp.dto.UserResponse;

public interface UserService {

    UserResponse createClerk(CreateClerkRequest request);

}