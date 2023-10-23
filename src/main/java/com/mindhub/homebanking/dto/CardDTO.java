package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDate;

public class CardDTO {
    private Long id;
    private String cardHolder, number, cvv;
    private LocalDate fromDate, thruDate;
    private CardColor color;
    private CardType type;

    public CardDTO() {
    }
    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardHolder = card.getCardHolder();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
        this.color = card.getColor();
        this.type = card.getType();
    }

    public Long getId() {
        return id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getNumber() {
        return number;
    }

    public String getCvv() {
        return cvv;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public CardColor getColor() {
        return color;
    }

    public CardType getType() {
        return type;
    }

}
