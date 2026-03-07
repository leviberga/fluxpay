package com.leviberga.fluxpay.service;

import com.leviberga.fluxpay.dto.UserRequestDTO;
import com.leviberga.fluxpay.dto.UserResponseDTO;
import com.leviberga.fluxpay.model.User;
import com.leviberga.fluxpay.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class UserService {


    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO){

       User user = new User();
       user.setName(userRequestDTO.getName());
       user.setDocument(userRequestDTO.getDocument());
       user.setEmail(userRequestDTO.getEmail());
       user.setPassword(userRequestDTO.getPassword());
       user.setBalance(BigDecimal.ZERO);
       user.setUserType(userRequestDTO.getUserType());

       User savedUser = userRepository.save(user);

       UserResponseDTO response = new UserResponseDTO();
       response.setId(savedUser.getId());
       response.setName(savedUser.getName());
       response.setEmail(savedUser.getEmail());
       response.setUserType(savedUser.getUserType());
       response.setBalance(savedUser.getBalance());

       return response;

    }

}
