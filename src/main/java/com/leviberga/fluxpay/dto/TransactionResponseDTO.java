package com.leviberga.fluxpay.dto;

import com.leviberga.fluxpay.model.Transaction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class TransactionResponseDTO {
    private UUID transactionID;
    private BigDecimal amount;
    private UserSummaryDTO sender;
    private UserSummaryDTO receiver;
    private LocalDateTime createdAt;

    public TransactionResponseDTO(Transaction transaction) {
        this.transactionID = transaction.getId();
        this.amount = transaction.getAmount();
        this.createdAt = transaction.getCreatedAt() != null ? transaction.getCreatedAt() : LocalDateTime.now();
        this.sender = new UserSummaryDTO(transaction.getSender());
        this.receiver = new UserSummaryDTO(transaction.getReceiver());
    }
}