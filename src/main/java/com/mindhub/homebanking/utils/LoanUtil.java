package com.mindhub.homebanking.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class LoanUtil {

    public static String formatterStringStartUpperEndLower(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

}
