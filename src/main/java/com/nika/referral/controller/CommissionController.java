package com.nika.referral.controller;

import com.nika.referral.model.Commission;
import com.nika.referral.repository.CommissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * Provides APIs to view commission transactions for users.
 *
 * @author abhisheks-gh
 */
@RestController
@RequestMapping("/api/commissions")
@RequiredArgsConstructor
public class CommissionController {

    private final CommissionRepository commissionRepository;

    @GetMapping
    List<Commission> getAllCommissions() {
        return commissionRepository.findAll();
    }

    @GetMapping("/user/{userId}")
    public List<Commission> getUserCommissions(@PathVariable Long userId) {
        return commissionRepository.findByUserId(userId);
    }
}
