package com.mindhub.homebanking.DTO;

import com.mindhub.homebanking.models.Loan;

import java.util.List;

public class LoanDTO {

    // Properties

    private Long id;

    private String name;

    private Double maxAmount, interestRate;

    private List<Integer> payments;

    // Method Constructor

    public LoanDTO(Loan loan) {
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.interestRate = loan.getInterestRate();
        this.payments = loan.getPayments();
    }

    // Accessory methods

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public List<Integer> getPayments() {
        return payments;
    }
}
