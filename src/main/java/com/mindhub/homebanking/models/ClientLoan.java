package com.mindhub.homebanking.models;

import jakarta.persistence.*;

@Entity
public class ClientLoan {

    // Properties

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    private int payments;

    private int paymentsMade = 0;

    private Double amountMade = 0.0;

    private Boolean isDeleted = false;

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

    public int getPaymentsMade() {
        return paymentsMade;
    }

    public void setPaymentsMade(int paymentsMade) {
        this.paymentsMade = paymentsMade;
    }

    public Double getAmountMade() {
        return amountMade;
    }

    public void setAmountMade(Double amountMade) {
        this.amountMade = amountMade;
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

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }
}
