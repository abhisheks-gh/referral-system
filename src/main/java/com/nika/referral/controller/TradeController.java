package com.nika.referral.controller;

import com.nika.referral.service.CommissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Simulates a trade event that triggers commission and cashback distribution.
 *
 * @author abhisheks-gh
 */
@RestController
@RequestMapping("/api/webhook")
@RequiredArgsConstructor
public class TradeController {

    private final CommissionService commissionService;

    /**
     * Simulates a trade event (used to test commission distribution logic).
     */
    @PostMapping("/trade")
    public ResponseEntity<?> simulateTrade(@RequestBody Map<String, Object> payload) {
        // System.out.println("DEBUG raw payload: " + payload);
        try {
            // Extract with null checks
            Object userIdObj = payload.get("userId");
            Object tradeVolumeObj = payload.get("tradeVolume");
            Object feePercentObj = payload.get("feePercent");

            if (userIdObj == null || tradeVolumeObj == null || feePercentObj == null) {
                throw new IllegalArgumentException(
                        "Missing one or more required fields: userId, tradeVolume, feePercent"
                );
            }
            Long userId = Long.valueOf(userIdObj.toString());
            BigDecimal tradeVolume = new BigDecimal(tradeVolumeObj.toString());
            BigDecimal feePercent = new BigDecimal(feePercentObj.toString());

            commissionService.processTrade(userId, tradeVolume, feePercent);

            return ResponseEntity.ok(Map.of(
                    "message", "Trade processed successfully",
                    "userId", userId,
                    "tradeVolume", tradeVolume,
                    "feePercent", feePercent
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}
