package com.mindhub.homebanking.dto;

public class LoanApplicationDTO {

    private Long idLoan;
    private Double amount;
    private int payments;
    private String numberAccountTo;

    public LoanApplicationDTO(Long idLoan, Double amount, int payments, String numberAccountTo) {
        this.idLoan = idLoan;
        this.amount = amount;
        this.payments = payments;
        this.numberAccountTo = numberAccountTo;
    }

    public Long getIdLoan() {
        return idLoan;
    }

    public Double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public String getNumberAccountTo() {
        return numberAccountTo;
    }
}
