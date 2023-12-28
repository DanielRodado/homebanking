package com.mindhub.homebanking.Utils;

public final class AccountUtil {

    public static int generateRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static String generateAccountNumber() {
        int quantityOfNumbers = generateRandomNumber(3, 8);
        StringBuilder accountNumber;
        accountNumber = new StringBuilder();
        for (byte i = 0; i <= quantityOfNumbers; i++) {
            accountNumber.append(generateRandomNumber(0, 9));
        }
        return "VIN-" + accountNumber;
    }

}
