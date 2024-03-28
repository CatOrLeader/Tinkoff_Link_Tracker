package edu.java.scrapper.utils;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public final class DateTimeUtils {
    public static final int OFFSET_HOURS = 3;

    private DateTimeUtils() {
    }

    public static Timestamp parseFrom(OffsetDateTime dateTime) {
        return dateTime == null ? null : Timestamp.valueOf(
            dateTime.atZoneSameInstant(ZoneOffset.ofHours(OFFSET_HOURS)).toLocalDateTime()
        );
    }

    public static OffsetDateTime parseFrom(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime().atOffset(ZoneOffset.ofHours(OFFSET_HOURS));
    }
}
