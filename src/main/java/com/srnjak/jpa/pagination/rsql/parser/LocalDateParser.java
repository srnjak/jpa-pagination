package com.srnjak.jpa.pagination.rsql.parser;

import com.srnjak.jpa.pagination.rsql.ValueParser;

import java.time.LocalDate;

/**
 * Parser for the type {@link LocalDate}.
 */
public class LocalDateParser implements ValueParser<LocalDate> {

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate parse(String value) {
        return LocalDate.parse(value);
    }

}
