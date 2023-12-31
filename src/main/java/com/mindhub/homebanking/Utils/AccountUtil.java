package com.mindhub.homebanking.Utils;

import com.mindhub.homebanking.services.AccountService;

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

    public static String verifiedNumberAccountNotRepeatDB(AccountService accountService) {
        String accountNumber;
        do {
            accountNumber = generateAccountNumber();
        } while (accountService.existsAccountByNumber("VIN-" + accountNumber));
        return accountNumber;
    }

}
