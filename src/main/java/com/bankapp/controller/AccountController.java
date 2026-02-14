package com.bankapp.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankapp.dto.AccountResponse;
import com.bankapp.dto.CreateAccountRequest;
import com.bankapp.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public AccountResponse create(
            @RequestBody CreateAccountRequest request) {

        return accountService.createAccount(request);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public Page<AccountResponse> list(Pageable pageable) {
        return accountService.listAccounts(pageable);
    }
}
