package de.awtools.registration;

import java.time.LocalDateTime;

public class DateTimeJson {

    private LocalDateTime dateTime;

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public DateTimeJson setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        return this;
    }

}
