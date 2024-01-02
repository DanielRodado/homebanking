package com.mindhub.homebanking.DTO;

public record LoanApplicationDTO(Long idLoan, Double amount, int payments, String numberAccountTo) {
}
