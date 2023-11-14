package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;

public interface ClientLoanService {

    ClientLoan getClientLoanByid(Long id);
    boolean existsClientLoanById(Long id);
    boolean existsClientLoanByClientAndId(Client client, Long id);
    boolean existsClientLoanByIdAndPaymentsLessThan(Long id, int payments);
    boolean existsClientLoanByIdAndAmountLessThan(Long id, Double amount);
    void saveClientLoan(ClientLoan clientLoan);
    void deleteClientLoan(ClientLoan clientLoan);

}
