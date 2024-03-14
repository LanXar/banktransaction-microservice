package com.banktransaction.banktransactionmicroservice.controller;

import com.banktransaction.banktransactionmicroservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestParam Long sourceAccountId, @RequestParam Long targetAccountId, @RequestParam BigDecimal amount) {
        try {
            transactionService.processTransaction(sourceAccountId, targetAccountId, amount);
            return ResponseEntity.ok().body("Transaction successful");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
