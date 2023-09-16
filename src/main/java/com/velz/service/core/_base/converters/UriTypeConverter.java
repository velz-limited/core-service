package com.velz.service.core._base.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;

@Converter(autoApply = true)
public class UriTypeConverter implements AttributeConverter<URI, String> {

    @Override
    public String convertToDatabaseColumn(URI entityValue) {
        return (entityValue == null) ? null : entityValue.toString();
    }

    @Override
    public URI convertToEntityAttribute(String databaseValue) {
        return (StringUtils.isNotEmpty(databaseValue) ? URI.create(databaseValue.trim()) : null);
    }
}
