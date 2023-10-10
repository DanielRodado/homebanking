package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "generatorID")
    @GenericGenerator(name = "generatorID", strategy = "native")
    private Long ID;
    private String name;
    private Double maxAmount;
    @ElementCollection
    private List<Integer> payments = new ArrayList<>();

    @OneToMany(mappedBy = "loan", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    public Loan() {
    }

    public Loan(String name, Double maxAmount, List<Integer> payments) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
    }

    public Long getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public void addClientLoan(ClientLoan clientLoan){
        clientLoan.setLoan(this);
        this.clientLoans.add(clientLoan);
    }

    @JsonIgnore
    public List<Client> getClients() {
        return clientLoans.stream().map(clientLoan -> clientLoan.getClient()).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Loan{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", maxAmount=" + maxAmount +
                ", payments=" + payments +
                ", clientLoans=" + clientLoans +
                '}';
    }
}
