package com.leviberga.fluxpay.dto;

import com.leviberga.fluxpay.enums.UserType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String document;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotNull
    private UserType userType;
}
