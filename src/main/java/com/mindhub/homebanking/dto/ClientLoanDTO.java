package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {
    private Long id;
    private Long loanID;
    private String name;
    private Double amount;
    private int payments;

    private int paymentsMade = 0;

    public ClientLoanDTO() {
    }

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.loanID = clientLoan.getLoan().getId();
        this.name = clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
    }

    public Long getId() {
        return id;
    }

    public Long getLoanID() {
        return loanID;
    }

    public String getName() {
        return name;
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
}
