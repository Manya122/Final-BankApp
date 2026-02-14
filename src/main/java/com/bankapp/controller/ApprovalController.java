package com.bankapp.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankapp.entity.User;
import com.bankapp.entity.WithdrawalRequest;
import com.bankapp.service.ApprovalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/approvals")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;

    @GetMapping("/pending")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public Page<WithdrawalRequest> pending(Pageable pageable) {
        return approvalService.pending(pageable);
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public void approve(
            @PathVariable Long id,
            Authentication auth) {

        var manager = (User) auth.getPrincipal();
        approvalService.approve(id, manager.getId());
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public void reject(
            @PathVariable Long id,
            Authentication auth) {

        var manager = (User) auth.getPrincipal();
        approvalService.reject(id, manager.getId());
    }
}
