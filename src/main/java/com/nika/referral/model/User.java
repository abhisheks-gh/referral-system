package com.nika.referral.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a system user with referral, commission, cashback, and XP tracking details.
 *
 * @author abhisheks-gh
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String referralCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referrer_id")
    private User referrer; // self-referencing for referral chain

    @OneToMany(mappedBy = "referrer", cascade = CascadeType.ALL)
    private List<User> directReferrals;

    @Column(nullable = false)
    private Double commissionBalance = 0.0;

    @Column(nullable = false)
    private Double cashbackBalance = 0.0;

    @Column(nullable = false)
    private Double xp = 0.0; // XP as crypto-analogy metric

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}