package edu.java.scrapper.domain.repository.jpa.dto.converters;

import jakarta.persistence.AttributeConverter;
import java.net.URI;

public class UrlConverter implements AttributeConverter<URI, String> {
    @Override
    public String convertToDatabaseColumn(URI uri) {
        return uri != null ? uri.toString() : null;
    }

    @Override
    public URI convertToEntityAttribute(String string) {
        return string != null ? URI.create(string) : null;
    }
}
