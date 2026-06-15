package com.leviberga.fluxpay.service;

import com.leviberga.fluxpay.dto.UserRequestDTO;
import com.leviberga.fluxpay.dto.UserResponseDTO;
import com.leviberga.fluxpay.exception.UserNotFoundException;
import com.leviberga.fluxpay.model.User;
import com.leviberga.fluxpay.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        User user = new User(userRequestDTO);
        User savedUser = userRepository.save(user);

        return new UserResponseDTO(savedUser);
    }

    public UserResponseDTO getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID " + id));

        return new UserResponseDTO(user);
    }
}