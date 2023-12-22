package com.mindhub.homebanking.models;

import jakarta.persistence.*;

public class ClientLoan {

    // Properties

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    private int payments;

    @ManyToOne(fetch = FetchType.EAGER)
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    private Loan loan;

    // Methods Constructors

    public ClientLoan() {
    }

    public ClientLoan(Double amount, int payments) {
        this.amount = amount;
        this.payments = payments;
    }

    // Accessory methods

    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public int getPayments() {
        return payments;
    }

    public void setPayments(int payments) {
        this.payments = payments;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }
}
