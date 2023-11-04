package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;

public interface CardService {

    boolean existsCardByNumber(String number);

    boolean existsCardByClientAndTypeAndColor(Client client, String type, String color);

    void saveCard(Card card);

}
