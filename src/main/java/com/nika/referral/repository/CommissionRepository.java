package com.nika.referral.repository;

import com.nika.referral.model.Commission;
import com.nika.referral.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Handles retrieval of commission by user and claim status.
 *
 * @author abhisheks-gh
 */
public interface CommissionRepository extends JpaRepository<Commission, Long> {

    List<Commission> findByUserAndClaimed(User user, boolean claimed);
    List<Commission> findBySourceUser(User sourceUser);
}
