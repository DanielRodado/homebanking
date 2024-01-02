package com.mindhub.homebanking.DTO;

public record PayLoanApplicationDTO(Long clientLoanId, int payments, Double amountToPay, String accountNumber) {
}
