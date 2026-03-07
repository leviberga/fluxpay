package com.leviberga.fluxpay.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class TransactionResponseDTO {
    private UUID transactionID;
    private BigDecimal amount;
    private UserSummaryDTO sender;
    private UserSummaryDTO receiver;
    private LocalDateTime createdAt;
}
