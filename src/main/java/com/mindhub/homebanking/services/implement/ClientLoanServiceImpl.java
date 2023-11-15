package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.services.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientLoanServiceImpl implements ClientLoanService {

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Override
    public ClientLoan getClientLoanByid(Long id) {
        return clientLoanRepository.findById(id).orElse(null);
    }

    @Override
    public boolean existsClientLoanById(Long id) {
        return clientLoanRepository.existsById(id);
    }

    @Override
    public boolean existsClientLoanByClientAndId(Client client, Long id) {
        return clientLoanRepository.existsByClientAndId(client, id);
    }

    @Override
    public boolean existsClientLoanByIdAndPaymentsLessThan(Long id, int payments) {
        return clientLoanRepository.existsByIdAndPaymentsLessThan(id, payments);
    }

    @Override
    public boolean existsClientLoanByIdAndAmountLessThan(Long id, Double amount) {
        return clientLoanRepository.existsByIdAndAmountLessThan(id, amount);
    }

    @Override
    public boolean existsClientLoanByClientAndLoan(Client client, Loan loan) {
        return clientLoanRepository.existsByClientAndLoan(client, loan);
    }

    @Override
    public boolean existsClientLoanByIdAndPayments(Long id, int payments) {
        return clientLoanRepository.existsByIdAndPayments(id, payments);
    }

    @Override
    public void saveClientLoan(ClientLoan clientLoan) {
        clientLoanRepository.save(clientLoan);
    }

    @Override
    public void deleteClientLoan(ClientLoan clientLoan) {
        clientLoan.setDeleted(true);
        saveClientLoan(clientLoan);
    }
}
