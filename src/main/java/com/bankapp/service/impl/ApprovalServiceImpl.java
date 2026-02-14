package com.bankapp.service.impl;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bankapp.entity.ApprovalStatus;
import com.bankapp.entity.BankTransaction;
import com.bankapp.entity.TransactionType;
import com.bankapp.entity.WithdrawalRequest;
import com.bankapp.repository.AccountRepository;
import com.bankapp.repository.TransactionRepository;
import com.bankapp.repository.WithdrawalRequestRepository;
import com.bankapp.service.ApprovalService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ApprovalServiceImpl implements ApprovalService {

    private final WithdrawalRequestRepository withdrawRepo;
    private final AccountRepository accountRepo;
    private final TransactionRepository txRepo;

    @Override
    public Page<WithdrawalRequest> pending(Pageable pageable) {
        return withdrawRepo.findByStatus(
                ApprovalStatus.PENDING,
                pageable
        );
    }

    @Override
    public void approve(Long id, Long managerId) {

        var req = withdrawRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (req.getStatus() != ApprovalStatus.PENDING) {
            throw new RuntimeException("Already processed");
        }

        var account = accountRepo
                .lockByAccountNumber(req.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance().compareTo(req.getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        account.setBalance(
                account.getBalance().subtract(req.getAmount())
        );

        req.setStatus(ApprovalStatus.APPROVED);
        req.setApprovedBy(managerId);

        txRepo.save(
            BankTransaction.builder()
                .accountNumber(account.getAccountNumber())
                .type(TransactionType.WITHDRAWAL)
                .amount(req.getAmount())
                .timestamp(Instant.now())
                .performedBy(req.getRequestedBy())
                .approvalStatus(ApprovalStatus.APPROVED)
                .approvedBy(managerId)
                .build()
        );
    }

    @Override
    public void reject(Long id, Long managerId) {

        var req = withdrawRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (req.getStatus() != ApprovalStatus.PENDING) {
            throw new RuntimeException("Already processed");
        }

        req.setStatus(ApprovalStatus.REJECTED);
        req.setApprovedBy(managerId);
    }
}
