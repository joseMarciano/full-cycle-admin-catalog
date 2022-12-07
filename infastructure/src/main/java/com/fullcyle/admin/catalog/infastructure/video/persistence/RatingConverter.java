package com.fullcyle.admin.catalog.infastructure.video.persistence;

import com.fullcyle.admin.catalog.domain.video.Rating;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;

import static java.util.Objects.isNull;

@Converter(autoApply = true)
public class RatingConverter implements AttributeConverter<Rating, String> {
    @Override
    public String convertToDatabaseColumn(final Rating attribute) {
        return Optional.ofNullable(attribute)
                .map(Rating::getName)
                .orElse(null);
    }

    @Override
    public Rating convertToEntityAttribute(final String dbData) {
        if (isNull(dbData)) return null;

        return Rating.of(dbData).orElse(null);
    }
}
