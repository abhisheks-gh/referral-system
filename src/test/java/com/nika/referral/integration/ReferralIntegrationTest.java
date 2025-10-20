package com.nika.referral.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nika.referral.model.User;
import com.nika.referral.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Validates the complete referral and trade flow integration across all layers.
 *
 * @author abhisheks-gh
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ReferralIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User referrer;

    @BeforeEach
    void setUp() {
        // Create a base referrer in DB
        referrer = new User();
        referrer.setEmail("referrer@nika.com");
        referrer.setReferralCode("REF12345");
        userRepository.save(referrer);
    }

    @Test
    void testEndToEndReferralFlow() throws Exception {
        // Register new user via referral
        String registerJson = """
            {
              "email": "newuser@nika.com",
              "referralCode": "REF12345"
            }
        """;

        mockMvc.perform(post("/api/referral/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerJson))
                .andExpect(status().isOk());

        // Simulate trade webhook for the new user
        User newUser = userRepository.findByEmail("newuser@nika.com").orElseThrow();
        String tradeJson = """
            {
              "userId": %d,
              "tradeVolume": 500.0,
              "feePercent": 0.01
            }
        """.formatted(newUser.getId());

        mockMvc.perform(post("/api/webhook/trade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tradeJson))
                .andExpect(status().isOk());

        // Assert balances updated
        User refUpdated = userRepository.findByEmail("referrer@nika.com").orElseThrow();
        User newUpdated = userRepository.findByEmail("newuser@nika.com").orElseThrow();

        assertThat(refUpdated.getCommissionBalance()).isGreaterThan(0);
        assertThat(newUpdated.getCashbackBalance()).isGreaterThan(0);
    }
}
