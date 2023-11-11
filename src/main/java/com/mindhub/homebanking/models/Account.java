package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "generatorID")
    @GenericGenerator(name = "generatorID", strategy = "native")
    private Long id;

    private String number;

    private LocalDate creationDate;

    private Double balance;

    private Boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();

    public Account() {
    }

    public Account(String number, LocalDate creationDate, Double balance) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
    }

    public Client getClient() {
        return client;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Double getBalance() {
        return balance;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(Transaction transaction){
        transaction.setAccount(this);
        this.transactions.add(transaction);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", creationDate=" + creationDate +
                ", balance=" + balance +
                '}';
    }
}
