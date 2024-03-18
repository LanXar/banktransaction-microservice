package com.banktransaction.banktransactionmicroservice.controller;

import com.banktransaction.banktransactionmicroservice.service.TransactionService;
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
    public ResponseEntity<?> createTransaction(@RequestBody TransactionRequest transactionRequest) {
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

    // Inner class to handle request data
    static class TransactionRequest {
        private Long sourceAccountId;
        private Long targetAccountId;
        private BigDecimal amount;

        // Getters and setters are crucial for Spring to bind request body data
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
