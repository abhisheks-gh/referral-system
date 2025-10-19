package com.nika.referral.service;

import com.nika.referral.model.Commission;
import com.nika.referral.model.User;
import com.nika.referral.repository.CommissionRepository;
import com.nika.referral.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Handles commission and cashback distribution logic for trades.
 *
 * @author abhisheks-gh
 */
@Service
@RequiredArgsConstructor
public class CommissionService {

    private final UserRepository userRepository;
    private final CommissionRepository commissionRepository;

    /**
     * Distributes commission and cashback for a given trade event.
     * Simulates Web3 payout logic (XP used instead of tokens).
     */
    @Transactional
    public void processTrade(Long traderUserId, BigDecimal tradeVolume, BigDecimal feePercent) {
        User trader = userRepository.findById(traderUserId)
                .orElseThrow(() -> new IllegalArgumentException("Trader not found"));

        // Total fee = tradeVolume * feePercent
        BigDecimal totalFee = tradeVolume.multiply(feePercent);

        // Cashback: 10% of fee
        BigDecimal cashBack = totalFee.multiply(BigDecimal.valueOf(0.10));
        trader.setCashbackBalance(trader.getCashbackBalance() + cashBack.doubleValue());
        trader.setXp(trader.getXp() + cashBack.doubleValue());
        userRepository.save(trader);

        // Commission breakdown: 30% (L1), 3% (L2), 2% (L3)
        distributeReferralCommission(trader, totalFee);

        // Treasury gets remaining 55% (simulated only)
        // No DB records needed, but could be logged
    }


    private void distributeReferralCommission(User trader, BigDecimal totalFee) {
        User currentReferrer = trader.getReferrer();
        int level = 1;

        while (currentReferrer != null && level <= 3) {
            double percentage = switch (level) {
                case 1 -> 0.30;
                case 2 -> 0.03;
                case 3 -> 0.02;
                default -> 0.0;
            };

            BigDecimal commissionAmount = totalFee.multiply(BigDecimal.valueOf(percentage));

            // Update balance
            currentReferrer.setCommissionBalance(
                    currentReferrer.getCommissionBalance() + commissionAmount.doubleValue()
            );
            currentReferrer.setXp(currentReferrer.getXp() + commissionAmount.doubleValue());
            userRepository.save(currentReferrer);

            // Record commission event
            Commission commission = Commission.builder()
                    .user(currentReferrer)
                    .sourceUser(trader)
                    .level(level)
                    .amount(commissionAmount.doubleValue())
                    .tokenType("USDC")
                    .claimed(false)
                    .build();
            commissionRepository.save(commission);

            // Move up one referrer
            currentReferrer = currentReferrer.getReferrer();
            level++;
        }
    }
}
