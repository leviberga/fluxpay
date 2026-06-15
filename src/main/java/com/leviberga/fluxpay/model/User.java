package com.leviberga.fluxpay.model;

import com.leviberga.fluxpay.dto.UserRequestDTO;
import com.leviberga.fluxpay.enums.UserType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id @GeneratedValue @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "user_id")
    private UUID id;
    private String name;
    private String document;
    private String email;
    private String password;
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    private UserType userType;

    public User(UserRequestDTO dto) {
        this.name = dto.getName();
        this.document = dto.getDocument();
        this.email = dto.getEmail();
        this.password = dto.getPassword();
        this.userType = dto.getUserType();
        this.balance = java.math.BigDecimal.ZERO;
    }
}
