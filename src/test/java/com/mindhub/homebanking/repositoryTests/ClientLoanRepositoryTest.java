package com.mindhub.homebanking.repositoryTests;

import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClientLoanRepositoryTest {

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    private List<ClientLoan> clientLoans;

    @BeforeEach
    public void getAllClientLoans() {
        clientLoans = clientLoanRepository.findAll();
    }


    @Test
    public void existsAccounts() {
        assertThat(clientLoans, is(not(empty())));
    }

    @Test
    public void balanceGreaterThan() {
        assertThat(clientLoans, everyItem(hasProperty("amount", greaterThanOrEqualTo(15000.0))));
    }
}
