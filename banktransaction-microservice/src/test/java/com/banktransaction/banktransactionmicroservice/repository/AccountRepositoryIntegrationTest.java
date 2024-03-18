package com.banktransaction.banktransactionmicroservice.repository;

import com.banktransaction.banktransactionmicroservice.model.Account;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class AccountRepositoryIntegrationTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void whenSaveAccountRetrieveIt() {
        Account account = new Account();
        // Assuming Account class includes @Id with @GeneratedValue and proper constructor
        account.setBalance(new BigDecimal("1000"));
        account.setCurrency("USD");
        account.setCreatedAt(LocalDateTime.now()); // Manually set for test, if not automatically set by JPA

        // Save the account to the database
        Account savedAccount = accountRepository.save(account);

        // Retrieve the saved account using its ID
        Account foundAccount = accountRepository.findById(savedAccount.getId()).orElse(null);

        // Validate that the saved account has the expected properties
        assertNotNull(foundAccount, "Found account should not be null.");
        assertEquals(savedAccount.getBalance(), foundAccount.getBalance(), "The balance should match.");
        assertEquals(savedAccount.getCurrency(), foundAccount.getCurrency(), "The currency should match.");
        // Ensure createdAt is set
        assertNotNull(foundAccount.getCreatedAt(), "createdAt should be set.");
    }

    @Test
    public void whenFindByNonExistingId_thenReturnEmpty() {
        // Using a negative ID assuming it's not auto-generated IDs
        Optional<Account> foundAccount = accountRepository.findById(-99L);
        assertFalse(foundAccount.isPresent(), "Should not find an account with a non-existing ID.");
    }
}
