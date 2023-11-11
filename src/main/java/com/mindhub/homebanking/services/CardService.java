package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;

public interface CardService {

    Card getCardById(Long id);

    boolean existsCardById(Long id);

    boolean existsCardByNumber(String number);

    boolean existsCardByClientAndTypeAndColor(Client client, String type, String color);

    void deletedCardById(Long id);

    void saveCard(Card card);

}
