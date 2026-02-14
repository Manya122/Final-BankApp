package com.bankapp.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankapp.dto.DepositRequest;
import com.bankapp.dto.WithdrawRequest;
import com.bankapp.entity.BankTransaction;
import com.bankapp.entity.User;
import com.bankapp.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService txService;

    @PostMapping("/deposit")
    @PreAuthorize("hasAuthority('ROLE_MANAGER') or hasAuthority('ROLE_CLERK')")
    public void deposit(
            @RequestBody DepositRequest request,
            Authentication auth) {

        var user = (User) auth.getPrincipal();
        txService.deposit(request, user.getId());
    }

    @GetMapping("/history/{acc}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER') or hasAuthority('ROLE_CLERK')")
    public Page<BankTransaction> history(
            @PathVariable String acc,
            Pageable pageable) {

        return txService.history(acc, pageable);
    }
    
    
    @PostMapping("/withdraw")
    @PreAuthorize("hasAuthority('ROLE_MANAGER') or hasAuthority('ROLE_CLERK')")
    public void withdraw(
            @RequestBody WithdrawRequest request,
            Authentication auth) {

        var user = (User) auth.getPrincipal();
        txService.withdraw(request, user.getId());
    }

}
