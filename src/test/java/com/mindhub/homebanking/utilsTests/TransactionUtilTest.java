package com.mindhub.homebanking.utilsTests;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static com.mindhub.homebanking.utils.TransactionUtil.formattedLocalDateTime;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransactionUtilTest {

    @Test
    public void localDateTimeTest() {
        assertThat(formattedLocalDateTime(LocalDateTime.now()), notNullValue());
    }

}
