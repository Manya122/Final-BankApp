package com.bankapp.util;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

@Component
public class AccountNumberGenerator {

    private final SecureRandom random = new SecureRandom();

    public String generate() {
        long num = 1_000_000_000L + Math.abs(random.nextLong()) % 9_000_000_000L;
        return String.valueOf(num);
    }
}