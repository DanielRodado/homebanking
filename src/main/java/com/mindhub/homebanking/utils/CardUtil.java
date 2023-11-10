package com.mindhub.homebanking.utils;

public final class CardUtil {

    public static int generateRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

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

}
