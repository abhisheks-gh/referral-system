package com.nika.referral.service;

import com.nika.referral.model.Referral;
import com.nika.referral.model.User;
import com.nika.referral.repository.ReferralRepository;
import com.nika.referral.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author abhisheks-gh
 */
@Service
@RequiredArgsConstructor
public class ReferralService {

    private final UserRepository userRepository;
    private final ReferralRepository referralRepository;

    public String generateReferralCode(User user) {
        // If user already has one, return existing code
        if (user.getReferralCode() != null) {
            return user.getReferralCode();
        }
        String code = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        user.setReferralCode(code);
        userRepository.save(user);
        return code;
    }

    @Transactional
    public User registerUser(String email, String referralCode) {
        // Check if email already exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        User newUser = User.builder()
                .email(email)
                .commissionBalance(0.0)
                .cashbackBalance(0.0)
                .xp(0.0)
                .build();

        if (referralCode != null && !referralCode.isEmpty()) {
            User referrer = userRepository.findByReferralCode(referralCode)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid referral code"));

            // Prevent circular reference
            if (referrer.getEmail().equalsIgnoreCase(email)) {
                throw new IllegalArgumentException("User cannot refer themselves");
            }

            // Assign referrer to the user
            newUser.setReferrer(referrer);
            newUser = userRepository.save(newUser);

            // Create referral records up to 3 levels deep
            createReferralChain(referrer, newUser);
        } else {
            newUser = userRepository.save(newUser);
        }

        // Auto-generate referral code for the new user
        generateReferralCode(newUser);

        return newUser;
    }

    /**
     * Creates referral relationship up to 3 levels
     */
    private void createReferralChain(User referrer, User referee) {
        User currentReferrer = referrer;
        int level = 1;

        while (currentReferrer != null && level <= 3) {
            Referral referral = Referral.builder()
                    .referrer(referrer)
                    .referee(referee)
                    .level(level)
                    .build();
            referralRepository.save(referral);

            currentReferrer = currentReferrer.getReferrer();
            level++;
        }
    }

    public User getOrCreateUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .commissionBalance(0.0)
                            .cashbackBalance(0.0)
                            .xp(0.0)
                            .build();
                    return userRepository.save(newUser);
                });
    }
}
