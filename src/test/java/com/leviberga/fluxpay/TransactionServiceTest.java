package com.leviberga.fluxpay;

import com.leviberga.fluxpay.dto.TransactionResponseDTO;
import com.leviberga.fluxpay.enums.UserType;
import com.leviberga.fluxpay.exception.InsufficientBalanceException;
import com.leviberga.fluxpay.exception.UnauthorizedTransactionException;
import com.leviberga.fluxpay.exception.UserNotFoundException;
import com.leviberga.fluxpay.model.User;
import com.leviberga.fluxpay.repository.TransactionRepository;
import com.leviberga.fluxpay.repository.UserRepository;
import com.leviberga.fluxpay.service.AuthorizationService;
import com.leviberga.fluxpay.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AuthorizationService authorizationService;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void shouldTransferSuccessfully(){
        User mockSender = new User();
        mockSender.setId(UUID.randomUUID());
        mockSender.setUserType(UserType.COMMON);
        mockSender.setBalance(new BigDecimal("1000"));

        BigDecimal amount = new BigDecimal("100");

        User mockReceiver = new User();
        mockReceiver.setId(UUID.randomUUID());
        mockReceiver.setUserType(UserType.MERCHANT);
        mockReceiver.setBalance(new BigDecimal("0"));

        when(userRepository.findById(mockSender.getId())).thenReturn(Optional.of(mockSender));
        when(userRepository.findById(mockReceiver.getId())).thenReturn(Optional.of(mockReceiver));

        when(authorizationService.authorize()).thenReturn(true);
        TransactionResponseDTO result = transactionService.transferMoney(mockSender.getId(), mockReceiver.getId(), amount);

        assertNotNull(result);

    }
    @Test
    void senderNotFound(){
        User mockSender = new User();
        mockSender.setId(UUID.randomUUID());
        mockSender.setUserType(UserType.COMMON);
        mockSender.setBalance(new BigDecimal("1000"));

        BigDecimal amount = new BigDecimal("100");

        User mockReceiver = new User();
        mockReceiver.setId(UUID.randomUUID());
        mockReceiver.setUserType(UserType.MERCHANT);
        mockReceiver.setBalance(new BigDecimal("0"));

        when(userRepository.findById(mockSender.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            transactionService.transferMoney(mockSender.getId(), mockReceiver.getId(), amount);
        });
    }
    @Test
    void senderIsMerchant(){
        User mockSender = new User();
        mockSender.setId(UUID.randomUUID());
        mockSender.setUserType(UserType.MERCHANT);
        mockSender.setBalance(new BigDecimal("1000"));

        BigDecimal amount = new BigDecimal("100");

        User mockReceiver = new User();
        mockReceiver.setId(UUID.randomUUID());
        mockReceiver.setUserType(UserType.MERCHANT);
        mockReceiver.setBalance(new BigDecimal("0"));

        when(userRepository.findById(mockSender.getId())).thenReturn(Optional.of(mockSender));

        assertThrows(UnauthorizedTransactionException.class, () -> {
            transactionService.transferMoney(mockSender.getId(), mockReceiver.getId(), amount);
        });
    }
    @Test
    void insufficientBalance(){
        User mockSender = new User();
        mockSender.setId(UUID.randomUUID());
        mockSender.setUserType(UserType.COMMON);
        mockSender.setBalance(new BigDecimal("10"));

        BigDecimal amount = new BigDecimal("100");

        User mockReceiver = new User();
        mockReceiver.setId(UUID.randomUUID());
        mockReceiver.setUserType(UserType.MERCHANT);
        mockReceiver.setBalance(new BigDecimal("0"));

        when(userRepository.findById(mockSender.getId())).thenReturn(Optional.of(mockSender));

        assertThrows(InsufficientBalanceException.class, () -> {
            transactionService.transferMoney(mockSender.getId(), mockReceiver.getId(), amount);
        });
    }
    @Test
    void unauthorizedTransaction(){
        User mockSender = new User();
        mockSender.setId(UUID.randomUUID());
        mockSender.setUserType(UserType.COMMON);
        mockSender.setBalance(new BigDecimal("1000"));

        BigDecimal amount = new BigDecimal("100");

        User mockReceiver = new User();
        mockReceiver.setId(UUID.randomUUID());
        mockReceiver.setUserType(UserType.MERCHANT);
        mockReceiver.setBalance(new BigDecimal("0"));

        when(userRepository.findById(mockSender.getId())).thenReturn(Optional.of(mockSender));
        when(userRepository.findById(mockReceiver.getId())).thenReturn(Optional.of(mockReceiver));

        when(authorizationService.authorize()).thenThrow(new UnauthorizedTransactionException("Unauthorized"));

        assertThrows(UnauthorizedTransactionException.class, () -> {
            transactionService.transferMoney(mockSender.getId(), mockReceiver.getId(), amount);
        });

    }
}
