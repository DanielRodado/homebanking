package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "generatorID")
    @GenericGenerator(name = "generatorID", strategy = "native")
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private TransactionType type;
    private Double amount;
    private String description;
    private LocalDateTime date;

    private Double balanceAccountBeforeTransaction;

    private Boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public Transaction() {
    }

    public Transaction(TransactionType type, Double amount, Double balanceAccountBeforeTransaction,String description, LocalDateTime date) {
        this.type = type;
        this.amount = amount;
        this.balanceAccountBeforeTransaction = balanceAccountBeforeTransaction;
        this.description = description;
        this.date = date;
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

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Double getBalanceAccountBeforeTransaction() {
        return balanceAccountBeforeTransaction;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public Account getAccount() {
        return account;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setBalanceAccountBeforeTransaction(Double balanceAccountBeforeTransaction) {
        this.balanceAccountBeforeTransaction = balanceAccountBeforeTransaction;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
