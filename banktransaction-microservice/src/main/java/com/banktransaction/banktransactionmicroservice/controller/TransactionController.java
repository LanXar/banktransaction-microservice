package com.banktransaction.banktransactionmicroservice.controller;

import com.banktransaction.banktransactionmicroservice.service.TransactionService;

import jakarta.validation.Valid; // Import for validation
import jakarta.validation.constraints.NotNull; // Import for NotNull validation
import jakarta.validation.constraints.Positive; // Import for Positive validation

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transactions")
    public ResponseEntity<?> createTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {
        try {
            transactionService.processTransaction(
                    transactionRequest.getSourceAccountId(), 
                    transactionRequest.getTargetAccountId(), 
                    transactionRequest.getAmount());
            return ResponseEntity.ok("Transaction successful");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    static class TransactionRequest {
        @NotNull(message = "Source account ID cannot be null")
        private Long sourceAccountId;

        @NotNull(message = "Target account ID cannot be null")
        private Long targetAccountId;

        @NotNull(message = "Amount cannot be null")
        @Positive(message = "Amount must be greater than 0")
        private BigDecimal amount;

        public Long getSourceAccountId() {
            return sourceAccountId;
        }

        public void setSourceAccountId(Long sourceAccountId) {
            this.sourceAccountId = sourceAccountId;
        }

        public Long getTargetAccountId() {
            return targetAccountId;
        }

        public void setTargetAccountId(Long targetAccountId) {
            this.targetAccountId = targetAccountId;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }
    }
}
