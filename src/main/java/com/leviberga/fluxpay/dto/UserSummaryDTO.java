package com.leviberga.fluxpay.dto;

import com.leviberga.fluxpay.enums.UserType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserSummaryDTO {
    private UUID id;
    private String name;
    private UserType userType;
}
