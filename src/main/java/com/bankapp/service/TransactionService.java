package com.bankapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bankapp.dto.DepositRequest;
import com.bankapp.dto.WithdrawRequest;
import com.bankapp.entity.BankTransaction;

public interface TransactionService {

    void deposit(DepositRequest request, Long performedBy);
    void withdraw(WithdrawRequest request, Long userId);

    Page<BankTransaction> history(
            String accountNumber,
            Pageable pageable);
}
