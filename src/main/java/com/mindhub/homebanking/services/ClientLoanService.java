package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

public interface ClientLoanService {

    ClientLoan getClientLoanByid(Long id);
    boolean existsClientLoanById(Long id);
    boolean existsClientLoanByClientAndId(Client client, Long id);
    boolean existsClientLoanByIdAndPaymentsLessThan(Long id, int payments);
    boolean existsClientLoanByIdAndAmountLessThan(Long id, Double amount);
    boolean existsClientLoanByClientAndLoan(Client client, Loan loan);
    void saveClientLoan(ClientLoan clientLoan);
    void deleteClientLoan(ClientLoan clientLoan);

}
