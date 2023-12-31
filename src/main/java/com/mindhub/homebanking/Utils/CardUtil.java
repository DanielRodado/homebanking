package com.mindhub.homebanking.Utils;

import com.mindhub.homebanking.services.CardService;

import static com.mindhub.homebanking.Utils.AccountUtil.generateRandomNumber;

public final class CardUtil {

    public static String generateNumberCard() {
        StringBuilder cardNumber;
        cardNumber = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            cardNumber.append(generateRandomNumber(0, 9));
            if ((i + 1) % 4 == 0 && i != 15) cardNumber.append("-");
        };
        return cardNumber.toString();
    }
    public static String generateCvvCard() {
        StringBuilder cardNumber = new StringBuilder();
        for (byte i = 0; i <= 2; i++) {
            cardNumber.append(generateRandomNumber(0, 9));
        }
        return cardNumber.toString();
    }

    public static String generateNumberCardNotRepeatedDB(CardService cardService) {
        String cardNumber;
        do {
            cardNumber = generateNumberCard();
        } while (cardService.existsCardByNumber(cardNumber));
        return cardNumber;
    }

}
