package com.mindhub.homebanking.DTO;

public record PayWithCardApplicationDTO(String numberCard, String cvvCard, String description, Double amount) {
}
