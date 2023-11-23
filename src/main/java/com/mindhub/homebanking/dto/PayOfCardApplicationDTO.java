package com.mindhub.homebanking.dto;

public class PayOfCardApplicationDTO {

    private String numberCard, cvvCard, description;

    private Double amount;

    public PayOfCardApplicationDTO() {
    }

    public String getNumberCard() {
        return numberCard;
    }

    public String getCvvCard() {
        return cvvCard;
    }

    public String getDescription() {
        return description;
    }

    public Double getAmount() {
        return amount;
    }


}
