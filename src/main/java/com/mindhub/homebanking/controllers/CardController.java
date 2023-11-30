package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dto.PayWithCardApplicationDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.utils.CardUtil.generateCvvCard;
import static com.mindhub.homebanking.utils.CardUtil.generateNumberCard;
import static com.mindhub.homebanking.utils.LoanUtil.formattedLocalDateTime;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CardService cardService;

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

        if (cardService.existsCardByClientAndTypeAndColorAndIsDeleted(client, cardType, cardColor, false)) {
            return new ResponseEntity<>("You may not have more than one card of type " + cardType + " and color " + cardColor,
                    HttpStatus.FORBIDDEN);
        }

        String cardNumber;
        do {
            cardNumber = generateNumberCard();
        } while (cardService.existsCardByNumber(cardNumber));

        Card card = new Card(client.getFullName(), cardNumber, generateCvvCard(),
                LocalDate.now(), LocalDate.now().plusYears(5), CardColor.valueOf(cardColor), CardType.valueOf(cardType));

        client.addCard(card);
        cardService.saveCard(card);

        return new ResponseEntity<>("Created card!",HttpStatus.CREATED);
    }

    @PatchMapping("/clients/current/cards/delete")
    public ResponseEntity<String> deletedCard(@RequestParam Long cardId, Authentication currentClient) {

        if (!cardService.existsCardById(cardId)) {
            return new ResponseEntity<>("This card does not exist", HttpStatus.FORBIDDEN);
        }

        if (!cardService.existsCardByIdAndClient(cardId, clientService.getClientByEmail(currentClient.getName()))) {
            return new ResponseEntity<>("This card does not belong you", HttpStatus.FORBIDDEN);
        }

        cardService.softDeleteCardById(cardId);

        return new ResponseEntity<>("Card deleted!", HttpStatus.OK);
    }

    @PostMapping("/cards/pay")
    public ResponseEntity<String> payWithCard(@RequestBody PayWithCardApplicationDTO payWithCardApp) {

        if (!cardService.existsCardByNumber(payWithCardApp.getNumberCard())) {
            return new ResponseEntity<>("The card data entered are incorrect.", HttpStatus.FORBIDDEN);
        }

        if (cardService.existsCardByNumberAndThruDateBeforeToday(payWithCardApp.getNumberCard())) {
            return new ResponseEntity<>("The card data entered are incorrect.", HttpStatus.FORBIDDEN);
        }

        if (!cardService.existsCardByCvv(payWithCardApp.getCvvCard())) {
            return new ResponseEntity<>("The card data entered are incorrect.", HttpStatus.FORBIDDEN);
        }

        if (!cardService.existsCardByNumberAndCvv(payWithCardApp.getNumberCard(), payWithCardApp.getCvvCard())) {
            return new ResponseEntity<>("The card data entered are incorrect.", HttpStatus.FORBIDDEN);
        }

        Account account =
                accountService.getAccountOfClientWithGreaterBalance(cardService.getClientByNumberCard(payWithCardApp.getNumberCard()));

        if (account.getBalance() < payWithCardApp.getAmount()) {
            return new ResponseEntity<>("Does not have sufficient balance to pay.", HttpStatus.FORBIDDEN);
        }

        Transaction transaction = new Transaction(TransactionType.DEBIT, -payWithCardApp.getAmount(),
                account.getBalance()-payWithCardApp.getAmount(), payWithCardApp.getDescription(),
                formattedLocalDateTime(LocalDateTime.now()));

        account.addTransaction(transaction);
        transactionService.saveTransaction(transaction);

        account.setBalance(account.getBalance()-payWithCardApp.getAmount());
        accountService.saveAccount(account);


        return new ResponseEntity<>("Successfully paid for!", HttpStatus.OK);
    }
}
