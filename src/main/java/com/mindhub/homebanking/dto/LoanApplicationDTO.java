package com.mindhub.homebanking.dto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public class LoanApplicationDTO {

    private Long idLoan;
    private int amount;
    private int payments;
    private String numberAccountTo;

    public LoanApplicationDTO(Long idLoan, int amount, int payments, String numberAccountTo) {
        this.idLoan = idLoan;
        this.amount = amount;
        this.payments = payments;
        this.numberAccountTo = numberAccountTo;
    }

    public Long getIdLoan() {
        return idLoan;
    }

    public int getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public String getNumberAccountTo() {
        return numberAccountTo;
    }
}
