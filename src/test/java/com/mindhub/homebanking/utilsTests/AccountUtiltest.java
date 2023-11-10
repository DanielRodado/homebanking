package com.mindhub.homebanking.utilsTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.mindhub.homebanking.utils.AccountUtil.generateAccountNumber;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountUtiltest {

    private String numberAccount;

    private String numberAccountNotVIN;

    @BeforeEach
    public void number() {
        numberAccount = generateAccountNumber();
        numberAccountNotVIN = numberAccount.replace("VIN-", "");
    }

    @Test
    public void verified() {
        assertThat(numberAccount, startsWith("VIN"));
    }

    @Test
    public void verifiedNumberAccountThat() {
        assertThat(numberAccountNotVIN.length(), greaterThanOrEqualTo(3));
    }

    @Test
    public void verifiedNumberAccount() {
        assertThat(numberAccountNotVIN.length(), lessThanOrEqualTo(8));
    }
    @Test
    public void verifiedNumberAccountNotAZ() {
        assertThat(numberAccountNotVIN, not(matchesPattern("[A-Za-z]+")));
    }

}
