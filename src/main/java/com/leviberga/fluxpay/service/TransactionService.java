package com.leviberga.fluxpay.service;

import com.leviberga.fluxpay.dto.TransactionResponseDTO;
import com.leviberga.fluxpay.enums.UserType;
import com.leviberga.fluxpay.exception.*;
import com.leviberga.fluxpay.model.*;
import com.leviberga.fluxpay.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final AuthorizationService authorizationService;

    @Transactional
    public TransactionResponseDTO transferMoney(UUID senderId, UUID receiverId, BigDecimal amount) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new UserNotFoundException("Sender not found with ID: " + senderId));
        if (sender.getUserType() == UserType.MERCHANT) {
            throw new UnauthorizedTransactionException("The sender user type cannot be MERCHANT");
        }
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new UserNotFoundException("Receiver not found with ID: " + receiverId));

        authorizationService.authorize();

        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        Transaction transaction = transactionRepository.save(new Transaction(sender, receiver, amount));

        return new TransactionResponseDTO(transaction);
    }
}
