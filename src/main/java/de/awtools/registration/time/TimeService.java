package de.awtools.registration.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class TimeService {

    public LocalDateTime now() {
        return LocalDateTime.now();
    }

    public Date currently() {
        return new Date();
    }

    public static Date convertToDateViaInstant(ZoneId zoneId, LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(zoneId)
                .toInstant());
    }

    public static Date convertToDateViaInstant(ZoneId zoneId, LocalDateTime dateToConvert) {
        return java.util.Date.from(dateToConvert.atZone(zoneId).toInstant());
    }

}
