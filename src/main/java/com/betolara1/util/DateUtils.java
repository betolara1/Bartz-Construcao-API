package com.betolara1.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class DateUtils {
    private DateUtils() {} // Impede instanciação
    
    private static final String[] FORMATS = {"dd/MM/yyyy", "dd-MM-yyyy", "yyyy-MM-dd", "yyyy/MM/dd"};
    
    public static LocalDate parseDate(String dateString) {
        for (String format : FORMATS) {
            try {
                return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(format));
            } catch (DateTimeParseException e) {
                continue;
            }
        }
        throw new IllegalArgumentException("Formato de data inválido: " + dateString);
    }
}