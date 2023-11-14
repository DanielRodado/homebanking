package com.mindhub.homebanking.dto;

public class PayLoanApplicationDTO {

    private Long clientLoanId;
    private int payments;
    private Double amountToPay;
    private String AccountNumber;

    public PayLoanApplicationDTO() {
    }

    public Long getClientLoanId() {
        return clientLoanId;
    }

    public int getPayments() {
        return payments;
    }

    public Double getAmountToPay() {
        return amountToPay;
    }

    public String getAccountNumber() {
        return AccountNumber;
    }
}
