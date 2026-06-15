package com.leviberga.fluxpay.dto;

import com.leviberga.fluxpay.enums.UserType;
import com.leviberga.fluxpay.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class UserResponseDTO {
    private UUID id;
    private String name;
    private String email;
    private UserType userType;
    private BigDecimal balance;

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.userType = user.getUserType();
        this.balance = user.getBalance();
    }
}
