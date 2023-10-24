package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CardRepository cardRepository;

    public int generateRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public String validateCardNumber() {
        String cardNumber = "";
        while(cardRepository.existsByNumber(cardNumber) || cardNumber.isEmpty()) {
            for (int i = 0; i <= 3; i++) {
                cardNumber = generateRandomNumber(0, 999) + "-";
            }
        }
        return cardNumber;
    }
    public String validateCardCvv() {
        Integer cardNumber = generateRandomNumber(100, 999);

        while(cardRepository.existsByCvv(cardNumber.toString())) {
            cardNumber = generateRandomNumber(100, 999);
        }

        return cardNumber.toString();
    }

    @RequestMapping("/clients/currents/cards")
    public ResponseEntity<Object> newClient(@RequestParam CardColor cardColor, @RequestParam CardType cardType,
                                            Authentication authentication) {

        if (cardColor.toString().isEmpty()) {
            return new ResponseEntity<>("You must choose a card type.", HttpStatus.FORBIDDEN);
        }
        if (cardType.toString().isEmpty()) {
            return new ResponseEntity<>("You must choose a card color.", HttpStatus.FORBIDDEN);
        }

        if (cardRepository.countByType(cardType) > 3) {
            return new ResponseEntity<>("You cannot have more than three cards of the same type.", HttpStatus.FORBIDDEN);
        }

        Client client = clientRepository.findByEmail(authentication.getName());
        clientRepository.save(client);

        Card card = new Card(client.getFullName(), validateCardNumber(), validateCardCvv(),
                LocalDate.now(), LocalDate.now().plusYears(5), cardColor, cardType);

        client.addCard(card);
        cardRepository.save(card);
        clientRepository.save(client);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
