package com.mindhub.homebanking.repositoryTests;


import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
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
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    private List<Transaction> transactions;

    @BeforeEach
    public void getAllTransactions() {
        transactions = transactionRepository.findAll();
    }

    @Test
    public void existsTransactions() {
        assertThat(transactions, is(not(empty())));
    }

    @Test
    public void descriptionTransaction() {
        assertThat(transactions, everyItem(hasProperty("description", is(not(empty())))));
    }

    @Test
    public void verifiedTransactionCredit() {
        for (Transaction transaction: transactions) {
            if ("CREDIT".equals(transaction.getType().toString())) {
                assertThat(transaction, hasProperty("amount", greaterThan(0.0)));
            }
        }
    }

}
