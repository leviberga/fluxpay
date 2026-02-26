package com.leviberga.fluxpay.controller;

import com.leviberga.fluxpay.dto.TransactionRequestDTO;
import com.leviberga.fluxpay.model.Transaction;
import com.leviberga.fluxpay.repository.TransactionRepository;
import com.leviberga.fluxpay.repository.UserRepository;
import com.leviberga.fluxpay.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    private String newTransaction(@RequestBody TransactionRequestDTO newTransaction) {
        transactionService.transferMoney(newTransaction.getSenderId(), newTransaction.getReceiverId(), newTransaction.getAmount());
        return "Transfer successful";
    }
}
