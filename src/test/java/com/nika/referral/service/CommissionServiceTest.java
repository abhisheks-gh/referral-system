package com.nika.referral.service;

import com.nika.referral.model.Commission;
import com.nika.referral.model.User;
import com.nika.referral.repository.CommissionRepository;
import com.nika.referral.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Verifies commission and cashback distribution logic.
 *
 * @author abhisheks-gh
 */
public class CommissionServiceTest {

    @Mock
    private CommissionRepository commissionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommissionService commissionService;

    private User trader;
    private User level1Referrer;
    private User level2Referrer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create mock referral hierarchy
        level2Referrer = new User();
        level2Referrer.setId(1L);
        level2Referrer.setEmail("level2@nika.com");

        level1Referrer = new User();
        level1Referrer.setId(2L);
        level1Referrer.setEmail("level1@nika.com");
        level1Referrer.setReferrer(level2Referrer);

        trader = new User();
        trader.setId(3L);
        trader.setEmail("trader@nika.com");
        trader.setReferrer(level1Referrer);
    }

    @Test
    void testProcessTrade_ShouldDistributeCommissionAndCashbackCorrectly() {
        // Arrange
        BigDecimal tradeVolume = BigDecimal.valueOf(1000);
        BigDecimal feePercent = BigDecimal.valueOf(0.01); // 1% fee = 10.00 total fee
        BigDecimal totalFee = tradeVolume.multiply(BigDecimal.valueOf(0.01)); // 10.00
        BigDecimal cashback = totalFee.multiply(BigDecimal.valueOf(0.10)); // 1.00 cashback

        when(userRepository.findById(3L)).thenReturn(Optional.of(trader));
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(commissionRepository.save(any(Commission.class))).
                thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        commissionService.processTrade(3L, tradeVolume, feePercent);

        // Assert basic updates
        assertTrue(trader.getCashbackBalance() > 0,
                "Trader should receive cashback");
        assertTrue(level1Referrer.getCommissionBalance() > 0,
                "Level 1 referrer should earn commission");
        assertTrue(level2Referrer.getCommissionBalance() > 0,
                "Level 2 referrer should earn commission");

        // Verify persistence calls
        verify(userRepository, atLeastOnce()).save(any(User.class));
        verify(commissionRepository, atLeastOnce()).save(any(Commission.class));
    }

    @Test
    void testProcessTrade_WithInvalidUserId_ShouldThrowException() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                commissionService.processTrade(999L, BigDecimal.valueOf(1000), BigDecimal.valueOf(0.01))
        );
    }
}
