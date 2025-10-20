package com.nika.referral.service;

import com.nika.referral.model.User;
import com.nika.referral.repository.ReferralRepository;
import com.nika.referral.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests referral creation and user registration logic.
 *
 * @author abhisheks-gh
 */
public class ReferralServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReferralRepository referralRepository;

    @InjectMocks
    private ReferralService referralService;

    private User referrer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        referrer = new User();
        referrer.setId(1L);
        referrer.setEmail("referrer@nika.com");
        referrer.setReferralCode("ABC123");
    }

    @Test
    void testRegisterUser_WithValidReferralCode_ShouldAssignReferrer() {
        when(userRepository.findByReferralCode("ABC123")).thenReturn(Optional.of(referrer));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = referralService.registerUser("newuser@nika.com", "ABC123");

        assertNotNull(result, "User should not be null");
        assertEquals(referrer.getEmail(), result.getReferrer().getEmail());
        verify(userRepository, atLeastOnce()).save(any(User.class));
    }

    @Test
    void testRegisterUser_WithInvalidReferralCode_ShouldThrowException() {
        when(userRepository.findByReferralCode("INVALID")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                referralService.registerUser("test@nika.com", "INVALID")
        );
    }

    @Test
    void testRegisterUser_WithNoReferralCode_ShouldCreateUserWithoutReferrer() {
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = referralService.registerUser("noRef@nika.com", null);

        assertNotNull(result, "User should not be null");
        assertNull(result.getReferrer(), "User should have no referrer");
        verify(userRepository, atLeastOnce()).save(any(User.class));
    }
}
