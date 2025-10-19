package com.nika.referral.repository;

import com.nika.referral.model.Claim;
import com.nika.referral.model.User;

import java.util.List;

/**
 * Provide access to claim records for auditing and validation
 *
 * @author abhisheks-gh
 */
public interface ClaimRepository {

    List<Claim> findByUser(User user);
}
