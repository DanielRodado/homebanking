package com.mindhub.homebanking.RepositoryTest;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
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
public class CardRepositoryTest {

    @Autowired
    private CardRepository cardRepository;

    private List<Card> cards;

    @BeforeEach
    public void getAllCards() {
        cards = cardRepository.findAll();
    }

    @Test
    public void existsCards() {
        assertThat(cards, is(not(empty())));
    }

    @Test
    public void quantityCardsThanZero() {
        assertThat(cards, hasSize(greaterThan(5)));
    }

}
