package com.leviberga.fluxpay.service;

import com.leviberga.fluxpay.dto.TransactionResponseDTO;
import com.leviberga.fluxpay.dto.UserSummaryDTO;
import com.leviberga.fluxpay.enums.UserType;
import com.leviberga.fluxpay.exception.InsufficientBalanceException;
import com.leviberga.fluxpay.exception.UnauthorizedTransactionException;
import com.leviberga.fluxpay.exception.UserNotFoundException;
import com.leviberga.fluxpay.model.Transaction;
import com.leviberga.fluxpay.model.User;
import com.leviberga.fluxpay.repository.TransactionRepository;
import com.leviberga.fluxpay.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final AuthorizationService authorizationService;

    public TransactionService(UserRepository userRepository, TransactionRepository transactionRepository, AuthorizationService authorizationService) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.authorizationService = authorizationService;
    }

    @Transactional
    public TransactionResponseDTO transferMoney(UUID senderID, UUID receiverID, BigDecimal amount) {
        User sender = userRepository.findById(senderID)
                .orElseThrow(() -> new UserNotFoundException("Sender not found with the following ID: " + senderID));
        if (sender.getUserType() == UserType.MERCHANT){
            throw new UnauthorizedTransactionException("The sender user type cannot be MERCHANT");
        }
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
        User receiver = userRepository.findById(receiverID)
                .orElseThrow(() -> new UserNotFoundException("Receiver not found with the following ID: " + receiverID));

        authorizationService.authorize();

        BigDecimal newSenderBalance = sender.getBalance().subtract(amount);
        sender.setBalance(newSenderBalance);
        BigDecimal newReceiverBalance = receiver.getBalance().add(amount);
        receiver.setBalance(newReceiverBalance);

        userRepository.save(sender);
        userRepository.save(receiver);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setSender(sender);
        transaction.setReceiver(receiver);

        transactionRepository.save(transaction);

        TransactionResponseDTO response = new TransactionResponseDTO();
        response.setTransactionID(transaction.getId());
        response.setAmount(transaction.getAmount());
        response.setCreatedAt(LocalDateTime.now());

        UserSummaryDTO senderDTO = new UserSummaryDTO();

        senderDTO.setId(sender.getId());
        senderDTO.setName(sender.getName());
        senderDTO.setUserType(sender.getUserType());

        UserSummaryDTO receiverDTO = new UserSummaryDTO();

        receiverDTO.setId(receiver.getId());
        receiverDTO.setName(receiver.getName());
        receiverDTO.setUserType(receiver.getUserType());

        response.setSender(senderDTO);
        response.setReceiver(receiverDTO);

        return response;
    }
}