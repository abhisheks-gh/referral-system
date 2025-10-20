package com.nika.referral.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nika.referral.service.CommissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests webhook trade simulation endpoint.
 *
 * @author abhisheks-gh
 */
@WebMvcTest(TradeController.class)
public class TradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CommissionService commissionService;

    @Test
    void testSimulateTrade_Success() throws Exception {
        // Arrange
        Map<String, Object> payload = Map.of(
                "userId", 3,
                "tradeVolume", 1000.0,
                "feePercent", 0.01
        );

        // Mock service call (no exception thrown)
        Mockito.doNothing().when(commissionService)
                .processTrade(eq(3L), any(BigDecimal.class), any(BigDecimal.class));

        // Act & Assert
        mockMvc.perform(post("/api/webhook/trade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Trade processed successfully"))
                .andExpect(jsonPath("$.userId").value(3))
                .andExpect(jsonPath("$.tradeVolume").value(1000.0))
                .andExpect(jsonPath("$.feePercent").value(0.01));
    }

    @Test
    void testSimulateTrade_MissingFields_ShouldReturnBadRequest() throws Exception {
        // Arrange — missing required fields
        Map<String, Object> invalidPayload = Map.of("userId", 3);

        // Act & Assert
        mockMvc.perform(post("/api/webhook/trade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPayload)))
                .andExpect(status().isBadRequest());
    }
}
