package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;

import java.time.LocalDate;

public interface CardService {

    Card getCardById(Long id);

    Client getClientByNumberCard(String number);

    boolean existsCardById(Long id);

    boolean existsCardByNumber(String number);

    boolean existsCardByClientAndTypeAndColorAndIsDeleted(Client client, String type, String color, Boolean isDeleted);

    boolean existsCardByIdAndClient(Long id, Client client);

    boolean existsCardByCvv(String cvv);

    boolean existsCardByNumberAndCvv(String number, String cvv);

    boolean existsCardByNumberAndThruDateBeforeToday(String number);

    void softDeleteCardById(Long id);

    void saveCard(Card card);

}
