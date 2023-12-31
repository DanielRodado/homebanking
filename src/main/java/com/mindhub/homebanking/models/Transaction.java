package com.mindhub.homebanking.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Transaction {

    // Properties

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private TransactionType type;

    private Double amount;

    private String description;

    private LocalDateTime date;

    private Double balanceAccountBeforeTransaction;

    @ManyToOne
    private Account account;

    // Methods Constructors

    public Transaction() {
    }

    public Transaction(TransactionType transactionType, Double amount, Double balanceAccountBeforeTransaction,
                       String description, LocalDateTime date) {
        this.type = transactionType;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.balanceAccountBeforeTransaction = balanceAccountBeforeTransaction;
    }

    public Long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Double getBalanceAccountBeforeTransaction() {
        return balanceAccountBeforeTransaction;
    }

    public void setBalanceAccountBeforeTransaction(Double balanceAccountBeforeTransaction) {
        this.balanceAccountBeforeTransaction = balanceAccountBeforeTransaction;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", type=" + type +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", balanceAccountBeforeTransaction=" + balanceAccountBeforeTransaction +
                ", account=" + account +
                '}';
    }
}
