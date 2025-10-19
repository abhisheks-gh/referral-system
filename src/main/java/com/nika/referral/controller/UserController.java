package com.nika.referral.controller;

import com.nika.referral.model.User;
import com.nika.referral.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * Exposes user-related APIs for dashboard visibility and referral tree view.
 *
 * @author abhisheks-gh
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get direct referrals for a user.
     */
    @GetMapping("/{id}/referrals")
    public List<User> getUserReferrals(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(User::getDirectReferrals)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
