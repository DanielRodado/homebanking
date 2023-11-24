package com.mindhub.homebanking.utils;

public final class ClientUtil {

    public static boolean verifiedUserEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    public static boolean verifiedUserPassword(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*\\d)(?!.*[^a-zA-Z0-9]).{8,15}$");
    }

}
