package com.nika.referral.controller;

import com.nika.referral.model.User;
import com.nika.referral.service.ReferralService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Handles referral code generation and user registration APIs.
 *
 * @author abhisheks-gh
 */
@RestController
@RequestMapping("/api/referral")
@RequiredArgsConstructor
public class ReferralController {

    private final ReferralService referralService;

    /**
     * Generates unique referral code for a user.
     *
     * @param payload simple JSON object like {"email": "abhishek@nika.com"}
     * @return referral code
     */
    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateReferralCode(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");

        User user = referralService.getOrCreateUserByEmail(email);
        String referralCode = referralService.generateReferralCode(user);

        return ResponseEntity.ok(Map.of("referralCode", referralCode));
    }

    /**
     * Register new user with a referral code.
     *
     * @param payload simple JSON object like {"email": "abhishek@nika.com"}
     * @return a new User instance
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String referralCode = payload.get("referralCode"); // optional

        User newUser = referralService.registerUser(email, referralCode);
        return ResponseEntity.ok(newUser);
    }
}
