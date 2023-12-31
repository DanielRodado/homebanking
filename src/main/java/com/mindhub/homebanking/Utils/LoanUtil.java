package com.mindhub.homebanking.Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class LoanUtil {

    public static LocalDateTime formattedLocalDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dateTime.format(formatter);
        return LocalDateTime.parse(formattedDateTime, formatter);
    }

    public static String formatterStringStartUpperEndLower(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

}
