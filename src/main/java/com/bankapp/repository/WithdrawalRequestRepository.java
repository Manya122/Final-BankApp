package com.bankapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bankapp.entity.ApprovalStatus;
import com.bankapp.entity.WithdrawalRequest;

@Repository
public interface WithdrawalRequestRepository
        extends JpaRepository<WithdrawalRequest, Long> {

    Page<WithdrawalRequest> findByStatus(
            ApprovalStatus status,
            Pageable pageable
    );
}