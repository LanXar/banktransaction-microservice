package com.banktransaction.banktransactionmicroservice.service;

import com.banktransaction.banktransactionmicroservice.model.Account;
import com.banktransaction.banktransactionmicroservice.model.Transaction;
import com.banktransaction.banktransactionmicroservice.repository.AccountRepository;
import com.banktransaction.banktransactionmicroservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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

        Account sourceAccount = accountRepository.findById(sourceAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Source account does not exist."));
        Account targetAccount = accountRepository.findById(targetAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Target account does not exist."));

        if (sourceAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance in source account.");
        }

        // Deduct the amount from the source account
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
        // Add the amount to the target account
        targetAccount.setBalance(targetAccount.getBalance().add(amount));

        // Create and set up the new Transaction
        Transaction transaction = new Transaction();
        transaction.setSourceAccount(sourceAccount);
        transaction.setTargetAccount(targetAccount);
        transaction.setAmount(amount);
        transaction.setCurrency(sourceAccount.getCurrency()); // Assuming the same currency for simplicity

        // Record the transaction
        transactionRepository.save(transaction);

        // The accounts are updated automatically due to CascadeType.ALL in Account entity relationships
        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);
    }
}
