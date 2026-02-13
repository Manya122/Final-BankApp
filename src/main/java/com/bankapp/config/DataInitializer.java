package com.bankapp.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.bankapp.entity.User;
import com.bankapp.entity.UserRole;
import com.bankapp.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {

        if (repo.findByUsername("manager").isEmpty()) {

            repo.save(
                User.builder()
                    .username("manager")
                    .password(encoder.encode("manager123"))
                    .role(UserRole.MANAGER)
                    .active(true)
                    .build()
            );

            System.out.println("Manager user created");
        }
    }
}

