package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "generatorID")
    @GenericGenerator(name = "generatorID", strategy = "native")
    private Long id;
    private String cardHolder, number, cvv;
    private LocalDate fromDate, thruDate;
    private CardColor color;
    private CardType type;
    private Boolean isDeleted = false;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    public Card() {
    }

    public Card(String cardHolder, String number, String cvv, LocalDate fromDate, LocalDate thruDate, CardColor color,
                CardType type) {
        this.cardHolder = cardHolder;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.color = color;
        this.type = type;
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

    public Boolean getDeleted() {
        return isDeleted;
    }

    public Client getClient() {
        return client;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public void setClient(Client client) {
        this.client = client;
    }

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
                ", client=" + client +
                '}';
    }
}
