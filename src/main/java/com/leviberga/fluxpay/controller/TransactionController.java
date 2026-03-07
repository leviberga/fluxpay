package com.leviberga.fluxpay.controller;

import com.leviberga.fluxpay.dto.TransactionRequestDTO;
import com.leviberga.fluxpay.dto.TransactionResponseDTO;
import com.leviberga.fluxpay.model.Transaction;
import com.leviberga.fluxpay.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> newTransaction(@Valid @RequestBody TransactionRequestDTO newTransaction) {
        TransactionResponseDTO transaction = transactionService.transferMoney(newTransaction.getSenderId(), newTransaction.getReceiverId(), newTransaction.getAmount());

        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }
}
