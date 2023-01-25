package project.data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * A utility class for anything having to do with date conversion or formatting.
 */
public class DateConverter {

    private static final String DATE_FORMAT = "MM/dd/yyyy hh:mm:ss a";
    private static final DateTimeFormatter displayFormat = DateTimeFormatter.ofPattern(DATE_FORMAT, Locale.US);

    /**
     * Converts a LocalDateTime input into a ZonedDateTime, sets the zone to the user's locality, and then converts that time into UTC for storage.
     *
     * @param ldt - LocalDateTime : the raw LocalDateTime
     * @return ZonedDateTime zoned for UTC
     */
    public static ZonedDateTime convertSystemToUTC(LocalDateTime ldt) {
        ZonedDateTime systemTime = ldt.atZone(ZoneId.systemDefault());
        return systemTime.withZoneSameInstant(ZoneId.of("UTC"));
    }

    /**
     * Converts a ZonedDateTime input into a Timestamp object for storage in the MySQL Database.
     *
     * @param zdt - ZonedDateTime : the ZonedDateTime to be converted
     * @return Timestamp to be stored
     */
    public static java.sql.Timestamp convertUTCToSQL(ZonedDateTime zdt) {
        LocalDateTime sqlIntermediary = zdt.toLocalDateTime();
        return java.sql.Timestamp.valueOf(sqlIntermediary);
    }

    /**
     * Converts a LocalDateTime input into ZonedDateTime, zoned for the user's locality for display purposes.
     *
     * @param ldt - LocalDateTime : the LocalDateTime to convert
     * @return ZonedDateTime zoned for the user's system
     */
    public static ZonedDateTime convertUTCToSystem(LocalDateTime ldt) {
        ZonedDateTime utcTime = ldt.atZone(ZoneId.of("UTC"));
        return utcTime.withZoneSameInstant(ZoneId.systemDefault());
    }

    /**
     * Utilizes a DateTimeFormatter to produce a more readable format for any ZonedDateTime input.
     *
     * @param zdt - ZonedDateTime : the ZonedDateTime to be formatted
     * @return the formatted String
     */
    public static String format(ZonedDateTime zdt) {
        return displayFormat.format(zdt);
    }

}
