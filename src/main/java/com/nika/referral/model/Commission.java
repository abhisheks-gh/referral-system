package com.nika.referral.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Stores commission earnings for a user, generated from referred users' trades.
 *
 * @author abhisheks-gh
 */
@Entity
@Table(name = "commissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Commission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The user who earned this commission
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // The user whose trade generated this commission
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_user_id", nullable = false)
    private User sourceUser;

    // Level 1 (30%), Level 2 (3%), Level 3 (2%)
    @Column(nullable = false)
    private int level;

    // XP amount earned (acts as simulated crypto reward)
    @Column(nullable = false)
    private Double amount;

    // Token type (simulated as "USDC" in our setup
    @Column(nullable = false)
    private String tokenType = "USDC";

    // Whether the commission has been claimed
    @Column(nullable = false)
    private boolean claimed = false;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime claimedAt;
}
