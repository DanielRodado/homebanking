package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dto.LoanDTO;
import com.mindhub.homebanking.models.Loan;

import java.util.List;
import java.util.Set;

public interface LoanService {

    List<Loan> getAllLoans();

    Set<LoanDTO> getAllLoansDTO();

    Loan getLoanById(Long id);

    boolean existsLoanById(Long id);

    boolean existsLoanByName(String name);

    void saveLoan(Loan loan);

}
