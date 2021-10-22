package com.srnjak.jpa.pagination.rsql.parser;

import com.srnjak.jpa.pagination.rsql.ValueParser;

import java.time.LocalDateTime;

/**
 * Parser for the type {@link LocalDateTime}.
 */
public class LocalDateTimeParser implements ValueParser<LocalDateTime> {

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime parse(String value) {
        return LocalDateTime.parse(value);
    }

}
