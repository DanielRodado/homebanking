package com.mindhub.homebanking.utilsTests;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static com.mindhub.homebanking.utils.LoanUtil.formatterStringStartUpperEndLower;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LoanUtilTest {

    @Test
    public void formatterStringTest() {
        assertThat(formatterStringStartUpperEndLower("aMAZinG"), endsWith("g"));
    }

}
