package com.banktransaction.banktransactionmicroservice.service;

import com.banktransaction.banktransactionmicroservice.model.Account;
import com.banktransaction.banktransactionmicroservice.repository.AccountRepository;
import com.banktransaction.banktransactionmicroservice.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Account sourceAccount;
    private Account targetAccount;

    @BeforeEach
    void setUp() {
        // Setup accounts with predetermined values
        sourceAccount = new Account();
        sourceAccount.setId(1L);
        sourceAccount.setBalance(new BigDecimal("1000.00"));
        sourceAccount.setCurrency("USD");

        targetAccount = new Account();
        targetAccount.setId(2L);
        targetAccount.setBalance(new BigDecimal("500.00"));
        targetAccount.setCurrency("USD");

        // //Mock the accountRepository to return these accounts when findById is called
        lenient().when(accountRepository.findById(sourceAccount.getId())).thenReturn(Optional.of(sourceAccount));
        lenient().when(accountRepository.findById(targetAccount.getId())).thenReturn(Optional.of(targetAccount));        
    }

    @Test   //More than transaction amount
    void givenSufficientBalanceTransactionDebitSourceAndCreditTarget() {
        BigDecimal transactionAmount = new BigDecimal("200.00");

        transactionService.processTransaction(sourceAccount.getId(), targetAccount.getId(), transactionAmount);

        // Assert the balances have been updated correctly
        assertEquals(0, sourceAccount.getBalance().compareTo(new BigDecimal("800.00")), "Source account balance should be debited by the transaction amount.");
        assertEquals(0, targetAccount.getBalance().compareTo(new BigDecimal("700.00")), "Target account balance should be credited by the transaction amount.");
        
        // Verify that the transaction is saved
        verify(transactionRepository, times(1)).save(any());
    }

    @Test   //Less than transaction amount
    void givenInsufficientSourceBalanceTransactionThrowExceptionNoBalanceChange() {
        BigDecimal transactionAmount = new BigDecimal("2000.00");
        
        // Assert that an IllegalArgumentException is thrown
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            transactionService.processTransaction(sourceAccount.getId(), targetAccount.getId(), transactionAmount)
        );
        
        // Assert the exception message
        assertTrue(exception.getMessage().contains("Insufficient balance in source account."));

        // Assert the balances remain unchanged
        assertEquals(0, sourceAccount.getBalance().compareTo(new BigDecimal("1000.00")), "Source account balance should remain the same.");
        assertEquals(0, targetAccount.getBalance().compareTo(new BigDecimal("500.00")), "Target account balance should remain the same.");
    }

    @Test   //Same account transaction
    void givenSourceAndTargetAreSame_whenTransactionRequestIsReceived_thenThrowExceptionAndNoBalanceChange() {
        BigDecimal transactionAmount = new BigDecimal("100.00");

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
            transactionService.processTransaction(sourceAccount.getId(), sourceAccount.getId(), transactionAmount)
        );

        assertTrue(exception.getMessage().contains("Source and target accounts must be different."));
        assertEquals(new BigDecimal("1000.00"), sourceAccount.getBalance());
    }

    @Test   //Account does not exist
    void givenSourceOrTargetAccountDoesNotExistThrowExceptionAndNoBalanceChange() {
        Long nonExistentAccountId = 1111L;
        BigDecimal transactionAmount = new BigDecimal("100.00");

        // Setup for when source account does not exist
        lenient().when(accountRepository.findById(nonExistentAccountId)).thenReturn(Optional.empty());
        lenient().when(accountRepository.findById(targetAccount.getId())).thenReturn(Optional.of(targetAccount));

        // Attempt to process transaction where source account does not exist
        Exception sourceNotExistException = assertThrows(IllegalArgumentException.class, () ->
            transactionService.processTransaction(nonExistentAccountId, targetAccount.getId(), transactionAmount)
        );
        assertTrue(sourceNotExistException.getMessage().contains("Source account does not exist"));
        assertEquals(new BigDecimal("500.00"), targetAccount.getBalance(), "Target account balance should remain the same.");

        // Setup for when target account does not exist
        reset(accountRepository); // Reset mocks
        lenient().when(accountRepository.findById(sourceAccount.getId())).thenReturn(Optional.of(sourceAccount));
        lenient().when(accountRepository.findById(nonExistentAccountId)).thenReturn(Optional.empty());

        // Attempt to process transaction where target account does not exist
        Exception targetNotExistException = assertThrows(IllegalArgumentException.class, () ->
            transactionService.processTransaction(sourceAccount.getId(), nonExistentAccountId, transactionAmount)
        );
        assertTrue(targetNotExistException.getMessage().contains("Target account does not exist"));
        assertEquals(new BigDecimal("1000.00"), sourceAccount.getBalance(), "Source account balance should remain the same.");
    }
}



