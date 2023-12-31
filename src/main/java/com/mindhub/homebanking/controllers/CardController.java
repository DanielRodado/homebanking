package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.DTO.PayWithCardApplicationDTO;
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

import static com.mindhub.homebanking.Utils.CardUtil.*;
import static com.mindhub.homebanking.Utils.LoanUtil.formattedLocalDateTime;

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

        Card card = new Card(client.getFullName(), generateNumberCardNotRepeatedDB(cardService), generateCvvCard(),
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

        if (!cardService.existsCardByNumber(payWithCardApp.numberCard())) {
            return new ResponseEntity<>("The card data entered are incorrect.", HttpStatus.FORBIDDEN);
        }

        if (cardService.existsCardByNumberAndThruDateBeforeToday(payWithCardApp.numberCard())) {
            return new ResponseEntity<>("The card data entered are incorrect.", HttpStatus.FORBIDDEN);
        }

        if (!cardService.existsCardByCvv(payWithCardApp.cvvCard())) {
            return new ResponseEntity<>("The card data entered are incorrect.", HttpStatus.FORBIDDEN);
        }

        if (!cardService.existsCardByNumberAndCvv(payWithCardApp.numberCard(), payWithCardApp.cvvCard())) {
            return new ResponseEntity<>("The card data entered are incorrect.", HttpStatus.FORBIDDEN);
        }

        Account account =
                accountService.getAccountOfClientWithGreaterBalance(cardService.getClientByNumberCard(payWithCardApp.numberCard()));

        if (account.getBalance() < payWithCardApp.amount()) {
            return new ResponseEntity<>("Does not have sufficient balance to pay.", HttpStatus.FORBIDDEN);
        }

        Transaction transaction = new Transaction(TransactionType.DEBIT, -payWithCardApp.amount(),
                account.getBalance()-payWithCardApp.amount(), payWithCardApp.description(),
                formattedLocalDateTime(LocalDateTime.now())
        );

        account.addTransaction(transaction);
        transactionService.saveTransaction(transaction);

        account.setBalance(account.getBalance()-payWithCardApp.amount());
        accountService.saveAccount(account);


        return new ResponseEntity<>("Successfully paid for!", HttpStatus.OK);
    }
}