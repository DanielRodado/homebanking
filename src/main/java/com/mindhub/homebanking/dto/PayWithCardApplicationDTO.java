package com.mindhub.homebanking.dto;

public class PayWithCardApplicationDTO {

    private String numberCard, cvvCard, description;

    private Double amount;

    public PayWithCardApplicationDTO() {
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
