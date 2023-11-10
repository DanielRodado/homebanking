package com.mindhub.homebanking.utilsTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.mindhub.homebanking.utils.CardUtil.generateCvvCard;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CardUtilTest {

    private String numberCardCvv;

    @BeforeEach
    public void number() {
        numberCardCvv = generateCvvCard();
    }

    @Test
    public void verifiedNumberCardCvvLess() {
        assertThat(numberCardCvv.length(), lessThanOrEqualTo(3));
    }

    @Test
    public void verifiedCvv() {
        assertThat(numberCardCvv, not(matchesPattern("[A-Za-z]+")));
    }

}
