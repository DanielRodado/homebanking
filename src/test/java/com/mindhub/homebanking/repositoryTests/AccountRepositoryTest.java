package com.mindhub.homebanking.repositoryTests;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
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
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    private List<Account> accounts;

    private Account account;

    @BeforeEach
    public void getAnyDataOfAnyAccounts() {
        accounts = accountRepository.findAll();
        account = accountRepository.findByNumber("VIN-001");
    }

    @Test
    public void existsAccounts(){
        assertThat(accounts, is(not(empty())));
    }

    @Test
    public void accountBalanceThanZero(){
        assertThat(account, hasProperty("balance", greaterThanOrEqualTo(0.0)));
    }
}
