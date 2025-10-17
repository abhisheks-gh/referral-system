package com.nika.referral.repository;

import com.nika.referral.model.Referral;
import com.nika.referral.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Provides method to query referral relationships by referrer or referee.
 *
 * @author abhisheks-gh
 */
public interface ReferralRepository extends JpaRepository<Referral, Long> {

    List<Referral> findByReferrer(User referrer);
    List<Referral> findByReferee(User referee);
}
