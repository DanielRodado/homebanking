package com.mindhub.homebanking.dto;

import java.util.List;

public class NewLoanApplication {

    private String nameOfLoan;
    private Double maxAmount, interestRate;
    private List<Integer> payments;

    public NewLoanApplication() {
    }

    public String getNameOfLoan() {
        return nameOfLoan;
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
