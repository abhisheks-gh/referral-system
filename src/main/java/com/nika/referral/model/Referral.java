package com.nika.referral.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Maps the referrer–referee relationship and defines the referral level (1, 2, or 3).
 * referrer -> referee
 *
 * @author abhisheks-gh
 */
@Entity
@Table(name = "referrals",
        uniqueConstraints = @UniqueConstraint(columnNames = {"referred_id", "referee_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Referral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The user who referred someone
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referrer_id", nullable = false)
    private User referrer;

    // The user who was referred
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referee_id", nullable = false)
    private User referee;

    // Level 1 = direct, 2 = indirect, 3 = third-level
    @Column(nullable = false)
    private int level;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
