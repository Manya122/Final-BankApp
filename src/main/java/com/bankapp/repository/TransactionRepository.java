package com.bankapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bankapp.entity.BankTransaction;

@Repository
public interface TransactionRepository extends JpaRepository<BankTransaction, Long> {

    Page<BankTransaction> findByAccountNumberOrderByTimestampDesc(
            String accountNumber,
            Pageable pageable
    );
}