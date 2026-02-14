package com.bankapp.dto;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountResponse {

    private String accountNumber;
    private String holderName;
    private BigDecimal balance;
    private Instant createdAt;
}