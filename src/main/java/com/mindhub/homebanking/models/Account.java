package com.mindhub.homebanking.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private Long id;

    private String number;

    private LocalDate creationDate;

    private Double balance;

    private AccountType accountType;

    private Boolean isDeleted = false;

    @ManyToOne()
    private Client client;

    public Account() {
    }

    public Account(String number, LocalDate creationDate, Double balance, AccountType accountType) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.accountType = accountType;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
