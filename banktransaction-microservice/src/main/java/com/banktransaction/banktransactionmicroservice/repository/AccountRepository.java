package com.banktransaction.banktransactionmicroservice.repository;

import com.banktransaction.banktransactionmicroservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
