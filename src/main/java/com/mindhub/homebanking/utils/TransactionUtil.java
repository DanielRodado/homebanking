package com.mindhub.homebanking.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class TransactionUtil {

    public static LocalDateTime formattedLocalDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dateTime.format(formatter);
        return LocalDateTime.parse(formattedDateTime, formatter);
    }

}
