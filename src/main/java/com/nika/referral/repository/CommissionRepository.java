package com.nika.referral.repository;

import com.nika.referral.model.Commission;
import com.nika.referral.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Handles retrieval of commission by userId, user and claim status.
 *
 * @author abhisheks-gh
 */
public interface CommissionRepository extends JpaRepository<Commission, Long> {

    List<Commission> findByUserId(Long userId);
    List<Commission> findByUserAndClaimed(User user, boolean claimed);
    List<Commission> findBySourceUser(User sourceUser);
}
