package com.srnjak.jpa.pagination.rsql.parser;

import com.srnjak.jpa.pagination.rsql.ValueParser;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Parser for the type {@link Timestamp}.
 */
public class SqlTimestampParser implements ValueParser<Timestamp> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp parse(String value) {
        return Timestamp.valueOf(LocalDateTime.parse(value));
    }

}
