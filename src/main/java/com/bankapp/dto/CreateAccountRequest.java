package com.bankapp.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccountRequest {

    private String holderName;
    private BigDecimal openingBalance;
}