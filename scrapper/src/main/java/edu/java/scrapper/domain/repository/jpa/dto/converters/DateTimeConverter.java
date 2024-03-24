package edu.java.scrapper.domain.repository.jpa.dto.converters;

import edu.java.scrapper.utils.DateTimeUtils;
import jakarta.persistence.AttributeConverter;
import java.sql.Timestamp;
import java.time.OffsetDateTime;

public class DateTimeConverter implements AttributeConverter<OffsetDateTime, Timestamp> {
    @Override
    public Timestamp convertToDatabaseColumn(OffsetDateTime dateTime) {
        return DateTimeUtils.parseFrom(dateTime);
    }

    @Override
    public OffsetDateTime convertToEntityAttribute(Timestamp timestamp) {
        return DateTimeUtils.parseFrom(timestamp);
    }
}
