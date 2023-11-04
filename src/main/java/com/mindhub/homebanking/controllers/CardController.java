package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private CardService cardService;

    public int generateRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public String generateNumberCard() {
        StringBuilder cardNumber;
        do {
            cardNumber = new StringBuilder();
            for (int i = 0; i < 16; i++) {
                cardNumber.append(generateRandomNumber(0, 9));
                if ((i + 1) % 4 == 0 && i != 15) cardNumber.append("-");
            }
        } while (cardService.existsCardByNumber(cardNumber.toString()));
        return cardNumber.toString();
    }
    public String generateCvvCard() {
        StringBuilder cardNumber = new StringBuilder();
        for (byte i = 0; i <= 2; i++) {
            cardNumber.append(generateRandomNumber(0, 9));
        }
        return cardNumber.toString();
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<String> newCard(@RequestParam String cardColor, @RequestParam String cardType,
                                          Authentication currentClient) {

        if (cardType.isBlank() || !(cardType.equals("DEBIT") || cardType.equals("CREDIT"))) {
            return new ResponseEntity<>("You must choose a card type.", HttpStatus.FORBIDDEN);
        }

        if (cardColor.isBlank() || !(cardColor.equals("GOLD") || cardColor.equals("TITANIUM") || cardColor.equals(
                "SILVER"))) {
            return new ResponseEntity<>("You must choose a card color.", HttpStatus.FORBIDDEN);
        }

        Client client = clientService.getClientByEmail(currentClient.getName());

        if (cardService.existsCardByClientAndTypeAndColor(client, cardType, cardColor)) {
            return new ResponseEntity<>("You may not have more than one card of type " + cardType + " and color " + cardColor,
                    HttpStatus.FORBIDDEN);
        }

        Card card = new Card(client.getFullName(), generateNumberCard(), generateCvvCard(),
                LocalDate.now(), LocalDate.now().plusYears(5), CardColor.valueOf(cardColor), CardType.valueOf(cardType));

        client.addCard(card);
        cardService.saveCard(card);

        return new ResponseEntity<>("Created card!",HttpStatus.CREATED);
    }

}
