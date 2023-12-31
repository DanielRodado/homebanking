package com.mindhub.homebanking.DTO;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

public class TransactionDTO {

    // Properties

    private Long id;

    private TransactionType type;

    private Double amount, balanceAccountBeforeTransaction;

    private String description;

    private LocalDateTime date;

    // Method Constructor

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.balanceAccountBeforeTransaction = transaction.getBalanceAccountBeforeTransaction();
        this.description = transaction.getDescription();
        this.date = transaction.getDate();
    }

    // Accessory methods

    public Long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public Double getAmount() {
        return amount;
    }

    public Double getBalanceAccountBeforeTransaction() {
        return balanceAccountBeforeTransaction;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
