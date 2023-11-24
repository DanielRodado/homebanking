package com.mindhub.homebanking.utilsTests;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.mindhub.homebanking.utils.ClientUtil.verifiedUserEmail;
import static com.mindhub.homebanking.utils.ClientUtil.verifiedUserPassword;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClientUtilTest {

    @Test
    public void testVerifiedEmail() {
        assertThat(verifiedUserEmail("danielrodado@gmail.com"), is(true));
    }

    @Test
    public void testVerifiedPassword() {
        assertThat(verifiedUserPassword("americaN12"), is(true));
    }

}
