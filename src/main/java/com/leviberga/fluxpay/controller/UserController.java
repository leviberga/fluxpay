package com.leviberga.fluxpay.controller;

import com.leviberga.fluxpay.dto.UserRequestDTO;
import com.leviberga.fluxpay.dto.UserResponseDTO;
import com.leviberga.fluxpay.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping
    public ResponseEntity<UserResponseDTO> newUser(@Valid @RequestBody UserRequestDTO newUser) {
        UserResponseDTO user = userService.createUser(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
    @GetMapping("/{Id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable UUID Id){
        UserResponseDTO user = userService.getUserById(Id);

        return ResponseEntity.status(HttpStatus.OK).body(user);

    }
}
