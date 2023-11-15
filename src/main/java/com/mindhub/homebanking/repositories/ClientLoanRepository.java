package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientLoanRepository extends JpaRepository<ClientLoan, Long> {

    boolean existsByClientAndId(Client client, Long id);

    boolean existsByIdAndAmountLessThan(Long id, Double amount);

    boolean existsByIdAndPaymentsLessThan(Long id, int payments);

    boolean existsByClientAndLoan(Client client, Loan loan);
    boolean existsByIdAndPayments(Long id, int payments);


}
