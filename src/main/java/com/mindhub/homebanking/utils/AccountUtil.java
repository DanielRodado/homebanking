package com.mindhub.homebanking.utils;

import static com.mindhub.homebanking.utils.CardUtil.generateRandomNumber;

public final class AccountUtil {

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
