package com.leviberga.fluxpay.service;

import com.leviberga.fluxpay.enums.UserType;
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

    @Autowired
    public TransactionService(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void transferMoney(UUID senderID, UUID receiverID, BigDecimal amount) {
        User sender = userRepository.findById(senderID)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        if (sender.getUserType() == UserType.MERCHANT){
            throw new RuntimeException("The sender user type cannot be MERCHANT");
        }
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        User receiver = userRepository.findById(receiverID)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

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