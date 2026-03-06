package com.leviberga.fluxpay.service;

import com.leviberga.fluxpay.enums.UserType;
import com.leviberga.fluxpay.exception.InsufficientBalanceException;
import com.leviberga.fluxpay.exception.ReceiverNotFoundException;
import com.leviberga.fluxpay.exception.UnauthorizedTransactionException;
import com.leviberga.fluxpay.exception.UserNotFoundException;
import com.leviberga.fluxpay.model.Transaction;
import com.leviberga.fluxpay.model.User;
import com.leviberga.fluxpay.repository.TransactionRepository;
import com.leviberga.fluxpay.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final AuthorizationService authorizationService;

    @Autowired
    public TransactionService(UserRepository userRepository, TransactionRepository transactionRepository, AuthorizationService authorizationService) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.authorizationService = authorizationService;
    }

    @Transactional
    public void transferMoney(UUID senderID, UUID receiverID, BigDecimal amount) {
        User sender = userRepository.findById(senderID)
                .orElseThrow(() -> new UserNotFoundException("Sender not found with the following ID: " + senderID));
        if (sender.getUserType() == UserType.MERCHANT){
            throw new UnauthorizedTransactionException("The sender user type cannot be MERCHANT");
        }
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance, the sender has " + sender.getBalance() + " and it needed atleast " + amount);
        }
        User receiver = userRepository.findById(receiverID)
                .orElseThrow(() -> new ReceiverNotFoundException("Receiver not found with the following ID: " + receiverID));

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
    }
}