package utils.date;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for date and time operations.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateUtils {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss_SSS");
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter LOG_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Returns a formatted timestamp string for filenames.
     * Format: yyyy_MM_dd_HH_mm_ss_SSS
     */
    public static String getCurrentTimestamp() {
        return LocalDateTime.now().format(TIMESTAMP_FORMATTER);
    }

    /**
     * Returns the current date as a string.
     * Format: yyyy-MM-dd
     */
    public static String getCurrentDate() {
        return LocalDateTime.now().format(DATE_FORMATTER);
    }

    /**
     * Returns the current date-time for log messages.
     * Format: yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentLogTimestamp() {
        return LocalDateTime.now().format(LOG_FORMATTER);
    }

    /**
     * Formats a LocalDateTime to a custom pattern.
     *
     * @param dateTime the date-time to format
     * @param pattern  the pattern to use
     * @return the formatted string
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Creates a formatted timestamp suitable for test evidence filenames.
     *
     * @param testName the name of the test
     * @return a formatted string like "LoginTest_2025_05_01_15_35_11"
     */
    public static String getScreenshotFilename(String testName) {
        return testName + "_" + getCurrentTimestamp();
    }
}