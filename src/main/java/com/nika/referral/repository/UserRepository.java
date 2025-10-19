package com.nika.referral.repository;

import com.nika.referral.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Handles User entity CRUD and lookup operations.
 *
 * @author abhisheks-gh
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByReferralCode(String referralCode);
}
