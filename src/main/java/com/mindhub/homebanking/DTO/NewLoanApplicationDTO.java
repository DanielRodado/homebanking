package com.mindhub.homebanking.DTO;

import java.util.List;

public record NewLoanApplicationDTO(String nameOfLoan, Double maxAmount, Double interestRate, List<Integer> payments) {
}
