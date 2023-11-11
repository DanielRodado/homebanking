package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Override
    public Card getCardById(Long id) {
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public boolean existsCardById(Long id) {
        return cardRepository.existsById(id);
    }

    @Override
    public boolean existsCardByNumber(String number) {
        return cardRepository.existsByNumber(number);
    }

    @Override
    public boolean existsCardByClientAndTypeAndColor(Client client, String type, String color) {
        return cardRepository.existsByClientAndTypeAndColor(client, CardType.valueOf(type), CardColor.valueOf(color));
    }

    @Override
    public void deletedCardById(Long id) {
        Card card = getCardById(id);
        card.setDeleted(true);
        saveCard(card);
    }

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }
}
