package com.nika.referral.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Records each commission claimed by a user (audit of claimed amounts).
 *
 * @author abhisheks-gh
 */
@Entity
@Table(name = "claims")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The user who claimed their commissions
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Total XP/commission amount claimed in this action
    @Column(nullable = false)
    private Double claimedAmount;

    @Column(nullable = false)
    private String tokenType = "USDC";

    @Column(updatable = false)
    private LocalDateTime claimedAt = LocalDateTime.now();
}
