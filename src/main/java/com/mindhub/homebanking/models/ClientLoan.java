package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class ClientLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "generatorID")
    @GenericGenerator(name = "generatorID", strategy = "native")
    private Long id;

    private Double amount;

    private int payments;

    private int paymentsMade = 0;

    private Double amountMade = 0.0;

    @ManyToOne(fetch = FetchType.EAGER)
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    private Loan loan;

    public ClientLoan() {
    }

    public ClientLoan(Double amount, int payments) {
        this.amount = amount;
        this.payments = payments;
    }

    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public int getPaymentsMade() {
        return paymentsMade;
    }

    public Double getAmountMade() {
        return amountMade;
    }

    public Client getClient() {
        return client;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setPayments(int payments) {
        this.payments = payments;
    }

    public void setPaymentsMade(int paymentsMade) {
        this.paymentsMade = paymentsMade;
    }

    public void setAmountMade(Double amountMade) {
        this.amountMade = amountMade;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }
}
