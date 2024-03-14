package com.banktransaction.banktransactionmicroservice.service;

import com.banktransaction.banktransactionmicroservice.model.Account;
import com.banktransaction.banktransactionmicroservice.model.Transaction;
import com.banktransaction.banktransactionmicroservice.repository.AccountRepository;
import com.banktransaction.banktransactionmicroservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public void processTransaction(Long sourceAccountId, Long targetAccountId, BigDecimal amount) {
        if (sourceAccountId.equals(targetAccountId)) {
            throw new IllegalArgumentException("Source and target accounts must be different.");
        }

        Optional<Account> sourceAccount = accountRepository.findById(sourceAccountId);
        Optional<Account> targetAccount = accountRepository.findById(targetAccountId);

        if (sourceAccount.isEmpty() || targetAccount.isEmpty()) {
            throw new IllegalArgumentException("One or both accounts do not exist.");
        }

        if (sourceAccount.get().getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance.");
        }

        // Deduct from source
        sourceAccount.get().setBalance(sourceAccount.get().getBalance().subtract(amount));
        // Add to target
        targetAccount.get().setBalance(targetAccount.get().getBalance().add(amount));

        // Save the accounts
        accountRepository.save(sourceAccount.get());
        accountRepository.save(targetAccount.get());

        // Record the transaction
        Transaction transaction = new Transaction();
        transaction.setSourceAccountId(sourceAccountId);
        transaction.setTargetAccountId(targetAccountId);
        transaction.setAmount(amount);
        transaction.setCurrency(sourceAccount.get().getCurrency()); // Assuming same currency for simplicity
        transactionRepository.save(transaction);
    }
}

