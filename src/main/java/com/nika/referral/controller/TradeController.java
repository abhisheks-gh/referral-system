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
    public ResponseEntity<Map<String, Object>> simulateTrade(@RequestBody Map<String, Object> payload) {
        // System.out.println("DEBUG raw payload: " + payload);
        Long userId = Long.valueOf(payload.get("userId").toString());
        BigDecimal tradeVolume = new BigDecimal(payload.get("tradeVolume").toString());
        BigDecimal feePercent = new BigDecimal(payload.get("feePercent").toString());

        commissionService.processTrade(userId, tradeVolume, feePercent);

        return ResponseEntity.ok(Map.of(
                "message", "Trade processed successfully",
                "userId", userId,
                "tradeVolume", tradeVolume,
                "feePercent", feePercent
        ));
    }
}
