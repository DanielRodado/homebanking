package com.mindhub.homebanking.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Card {

    // Properties

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cardHolder, number, cvv;
    private LocalDate fromDate, thruDate;
    private CardColor color;
    private CardType type;
    private Boolean isDeleted = false;

    @ManyToOne
    private Client client;

    // Methods Constructors

    public Card() {
    }

    public Card(String cardHolder, String number, String cvv, LocalDate fromDate, LocalDate thruDate, CardColor color, CardType type) {
        this.cardHolder = cardHolder;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.color = color;
        this.type = type;
    }

    // Accessory Methods

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }

    public CardColor getColor() {
        return color;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    // Methods


    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", cardHolder='" + cardHolder + '\'' +
                ", number='" + number + '\'' +
                ", cvv='" + cvv + '\'' +
                ", fromDate=" + fromDate +
                ", thruDate=" + thruDate +
                ", color=" + color +
                ", type=" + type +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
