package com.leviberga.fluxpay.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class TransactionRequestDTO {
    @NotNull
    private UUID senderId;
    @NotNull
    private UUID receiverId;
    @Positive @NotNull
    private BigDecimal amount;
}
