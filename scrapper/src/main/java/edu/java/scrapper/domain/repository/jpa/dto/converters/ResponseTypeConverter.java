package edu.java.scrapper.domain.repository.jpa.dto.converters;

import edu.java.scrapper.domain.dto.ResponseType;
import jakarta.persistence.AttributeConverter;

public class ResponseTypeConverter implements AttributeConverter<ResponseType, String> {
    @Override
    public String convertToDatabaseColumn(ResponseType responseType) {
        return responseType != null ? responseType.name() : null;
    }

    @Override
    public ResponseType convertToEntityAttribute(String string) {
        return string != null ? ResponseType.valueOf(string) : null;
    }
}
