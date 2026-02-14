package com.bankapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bankapp.dto.AccountResponse;
import com.bankapp.dto.CreateAccountRequest;

public interface AccountService {

    AccountResponse createAccount(CreateAccountRequest request);

    Page<AccountResponse> listAccounts(Pageable pageable);
}