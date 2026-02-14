package com.bankapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bankapp.entity.WithdrawalRequest;

public interface ApprovalService {

    Page<WithdrawalRequest> pending(Pageable pageable);

    void approve(Long requestId, Long managerId);

    void reject(Long requestId, Long managerId);
}
