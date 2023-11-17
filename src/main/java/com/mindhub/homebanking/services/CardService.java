package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;

public interface CardService {

    Card getCardById(Long id);

    boolean existsCardById(Long id);

    boolean existsCardByNumber(String number);

    boolean existsCardByClientAndTypeAndColorAndIsDeleted(Client client, String type, String color, Boolean isDeleted);

    boolean existsCardByIdAndClient(Long id, Client client);

    void softDeleteCardById(Long id);

    void saveCard(Card card);

}
