package de.awtools.registration.time;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Some utilities to convert {@link java.util.Date} to {@link java.tim.LocalDate}.
 * 
 * @author winkler
 */
public class DateTimeUtils {

    public static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("UTC");

    public LocalDateTime toDate(Date date) {
        return date.toInstant().atZone(DEFAULT_ZONE_ID).toLocalDateTime();
    }

    public Date toDate(LocalDateTime dateTime) {
        return java.util.Date.from(dateTime.atZone(DEFAULT_ZONE_ID).toInstant());
    }

}
