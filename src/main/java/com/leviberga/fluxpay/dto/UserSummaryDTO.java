package com.leviberga.fluxpay.dto;

import com.leviberga.fluxpay.enums.UserType;
import com.leviberga.fluxpay.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserSummaryDTO {
    private UUID id;
    private String name;
    private UserType userType;

    public UserSummaryDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.userType = user.getUserType();
    }
}
