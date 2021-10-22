package com.srnjak.jpa.pagination.rsql.parser;

import com.srnjak.jpa.pagination.rsql.ValueParser;

import java.time.*;
import java.time.format.DateTimeParseException;

/**
 * Parser for the type {@link Instant}.
 */
public class InstantParser implements ValueParser<Instant> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Instant parse(String value) {

        ZonedDateTime zdt;
        try {
            zdt = ZonedDateTime.parse(value);
        } catch (DateTimeParseException e) {
            try {
                zdt = LocalDateTime.parse(value)
                        .atZone(ZoneOffset.UTC);
            } catch (DateTimeParseException e1) {
                zdt = LocalDate.parse(value)
                        .atTime(0, 0, 0)
                        .atZone(ZoneOffset.UTC);
            }
        }

        return zdt.toInstant();
    }
}
