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
    public boolean existsCardByClientAndTypeAndColorAndIsDeleted(
            Client client, String type, String color, Boolean isDeleted) {
        return cardRepository.existsByClientAndTypeAndColorAndIsDeleted(
                client, CardType.valueOf(type), CardColor.valueOf(color), isDeleted);
    }

    @Override
    public boolean existsCardByIdAndClient(Long id, Client client) {
        return cardRepository.existsByIdAndClient(id, client);
    }

    @Override
    public void softDeleteCardById(Long id) {
        cardRepository.softDeleteCardById(id);
    }

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }
}
