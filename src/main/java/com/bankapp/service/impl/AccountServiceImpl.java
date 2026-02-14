package com.bankapp.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bankapp.dto.AccountResponse;
import com.bankapp.dto.CreateAccountRequest;
import com.bankapp.entity.Account;
import com.bankapp.repository.AccountRepository;
import com.bankapp.service.AccountService;
import com.bankapp.util.AccountNumberGenerator;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repo;
    private final AccountNumberGenerator generator;

    @Override
    public AccountResponse createAccount(CreateAccountRequest req) {

        String accNum;

        // ensure uniqueness (rare collision guard)
        do {
            accNum = generator.generate();
        } while (repo.findByAccountNumber(accNum).isPresent());

        var acc = repo.save(
                Account.builder()
                        .accountNumber(accNum)
                        .holderName(req.getHolderName())
                        .balance(req.getOpeningBalance())
                        .build()
        );

        return AccountResponse.builder()
                .accountNumber(acc.getAccountNumber())
                .holderName(acc.getHolderName())
                .balance(acc.getBalance())
                .createdAt(acc.getCreatedAt())
                .build();
    }

    @Override
    public Page<AccountResponse> listAccounts(Pageable pageable) {

        return repo.findAll(pageable)
                .map(a -> AccountResponse.builder()
                        .accountNumber(a.getAccountNumber())
                        .holderName(a.getHolderName())
                        .balance(a.getBalance())
                        .createdAt(a.getCreatedAt())
                        .build());
    }
}
