package com.banktransaction.banktransactionmicroservice.repository;

import com.banktransaction.banktransactionmicroservice.model.Account;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void whenSaveAccount_thenRetrieveSame() {
        // Create an Account instance
        Account account = new Account();
        account.setBalance(new BigDecimal("1000"));
        account.setCurrency("USD");
        // Note: createdAt is automatically set in the Account model upon saving

        // Save the account to the database
        Account savedAccount = accountRepository.save(account);

        // Retrieve the saved account using its ID
        Account foundAccount = accountRepository.findById(savedAccount.getId()).orElse(null);

        // Validate that the saved account has the expected properties
        assertNotNull(foundAccount, "The found account should not be null");
        assertEquals(account.getBalance(), foundAccount.getBalance(), "The balance should match");
        assertEquals(account.getCurrency(), foundAccount.getCurrency(), "The currency should match");
        // createdAt is set
        assertNotNull(foundAccount.getCreatedAt(), "createdAt should be automatically set upon saving");
    }
}
