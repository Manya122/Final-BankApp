package com.bankapp.service.impl;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bankapp.dto.DepositRequest;
import com.bankapp.dto.WithdrawRequest;
import com.bankapp.entity.ApprovalStatus;
import com.bankapp.entity.BankTransaction;
import com.bankapp.entity.TransactionType;
import com.bankapp.repository.AccountRepository;
import com.bankapp.repository.TransactionRepository;
import com.bankapp.repository.WithdrawalRequestRepository;
import com.bankapp.service.TransactionService;
import com.bankapp.entity.WithdrawalRequest;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepo;
    private final TransactionRepository txRepo;
    private final WithdrawalRequestRepository withdrawRepo;
    
    private static final BigDecimal LIMIT =
            new BigDecimal("200000");

    @Override
    public void deposit(DepositRequest req, Long userId) {
    	
    	if (req.getAmount() == null || req.getAmount().signum() <= 0) {
    	    throw new RuntimeException("Amount must be positive");
    	}

        var account = accountRepo
                .lockByAccountNumber(req.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(
                account.getBalance().add(req.getAmount())
        );

        txRepo.save(
                BankTransaction.builder()
                        .accountNumber(account.getAccountNumber())
                        .type(TransactionType.DEPOSIT)
                        .amount(req.getAmount())
                        .timestamp(Instant.now())
                        .performedBy(userId)
                        .approvalStatus(ApprovalStatus.NONE)
                        .build()
        );
    }

    @Override
    public Page<BankTransaction> history(
            String accountNumber,
            Pageable pageable) {

        return txRepo
            .findByAccountNumberOrderByTimestampDesc(
                accountNumber,
                pageable
            );
    }

	@Override
	public void withdraw(WithdrawRequest req, Long userId) {
		if (req.getAmount() == null || req.getAmount().signum() <= 0) {
		    throw new RuntimeException("Amount must be positive");
		}
		
		if (req.getAmount().compareTo(LIMIT) <= 0) {
	        processImmediateWithdrawal(req, userId);
	    } else {
	        createApprovalRequest(req, userId);
	    }
		
	}
	
	private void processImmediateWithdrawal(
	        WithdrawRequest req,
	        Long userId) {

	    var account = accountRepo
	            .lockByAccountNumber(req.getAccountNumber())
	            .orElseThrow(() -> new RuntimeException("Account not found"));

	    if (account.getBalance().compareTo(req.getAmount()) < 0) {
	        throw new RuntimeException("Insufficient funds");
	    }

	    account.setBalance(
	            account.getBalance().subtract(req.getAmount())
	    );

	    txRepo.save(
	        BankTransaction.builder()
	            .accountNumber(account.getAccountNumber())
	            .type(TransactionType.WITHDRAWAL)
	            .amount(req.getAmount())
	            .timestamp(Instant.now())
	            .performedBy(userId)
	            .approvalStatus(ApprovalStatus.NONE)
	            .build()
	    );
	}
	
	private void createApprovalRequest(
	        WithdrawRequest req,
	        Long userId) {

	    withdrawRepo.save(
	        WithdrawalRequest.builder()
	            .accountNumber(req.getAccountNumber())
	            .amount(req.getAmount())
	            .requestedBy(userId)
	            .status(ApprovalStatus.PENDING)
	            .build()
	    );
	}


    
    
}
