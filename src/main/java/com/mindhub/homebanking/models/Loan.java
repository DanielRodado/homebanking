package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Loan {

    // Properties

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double maxAmount, interestRate;

    private Boolean isDeleted = false;

    @ElementCollection
    private List<Integer> payments = new ArrayList<>();

    @OneToMany(mappedBy = "loan", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    // Methods Constructors

    public Loan() {
    }

    public Loan(String name, Double maxAmount, Double interestRate, List<Integer> payments) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.interestRate = interestRate;
        this.payments = payments;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    // Methods

    public void addClientLoan(ClientLoan clientLoan) {
        this.clientLoans.add(clientLoan);
        clientLoan.setLoan(this);
    }

    @JsonIgnore
    public Set<Client> getClients() {
        return this.clientLoans.stream().map(clientLoan -> clientLoan.getClient()).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", maxAmount=" + maxAmount +
                ", interestRate=" + interestRate +
                ", payments=" + payments +
                ", clientLoans=" + clientLoans +
                '}';
    }
}
