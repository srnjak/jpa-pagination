package com.srnjak.jpa.pagination.rsql.parser;

import com.srnjak.jpa.pagination.rsql.ValueParser;

import java.time.LocalTime;

/**
 * Parser for the type {@link LocalTime}.
 */
public class LocalTimeParser implements ValueParser<LocalTime> {

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalTime parse(String value) {
        return LocalTime.parse(value);
    }

}
