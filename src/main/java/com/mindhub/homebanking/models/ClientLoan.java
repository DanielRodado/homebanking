package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class ClientLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "generatorID")
    @GenericGenerator(name = "generatorID", strategy = "native")
    private Long ID;

    private Double amount;

    private int payments;

    @ManyToOne(fetch = FetchType.EAGER)
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    private Loan loan;

    public ClientLoan() {
    }

    public ClientLoan(Client client, Loan loan, Double amount, int payments) {
        this.amount = amount;
        this.payments = payments;
        this.client = client;
        this.loan = loan;
    }

    public Long getID() {
        return ID;
    }

    public Double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
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

    public void setClient(Client client) {
        this.client = client;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }
}
