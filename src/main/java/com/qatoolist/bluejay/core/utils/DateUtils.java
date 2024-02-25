package com.qatoolist.bluejay.core.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * DateUtils provides utility methods for formatting and retrieving the current date and time.
 */
public class DateUtils {

    /**
     * Formats the current date and time according to the specified pattern.
     *
     * @param pattern The date and time format string (e.g., "yyyy-MM-dd_HHmmss").
     * @return The formatted date and time string.
     * @throws DateTimeParseException if the pattern is invalid.
     */

    private DateUtils() {
        throw new IllegalStateException("Utility class");
    }
    public static String getCurrentDateTime(String pattern) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return now.format(formatter);
    }
}
