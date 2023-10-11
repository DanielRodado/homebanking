package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {
    private Long ID;
    private Long loanID;
    private String name;
    private Double amount;
    private int payments;

    public ClientLoanDTO() {
    }

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.ID = clientLoan.getID();
        this.loanID = clientLoan.getLoan().getID();
        this.name = clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
    }

    public Long getID() {
        return ID;
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
}
