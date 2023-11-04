package com.mindhub.homebanking.dto;

public class LoanApplicationDTO {

    private Long idLoan;
    private Double amount;
    private int payments;
    private String numberAccountTo;

    public LoanApplicationDTO() {
    }

    /*public LoanApplicationDTO(LoanApplicationDTO loanApplicationDTO) {
    }*/

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
