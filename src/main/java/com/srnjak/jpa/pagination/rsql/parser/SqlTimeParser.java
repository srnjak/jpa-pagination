package com.srnjak.jpa.pagination.rsql.parser;

import com.srnjak.jpa.pagination.rsql.ValueParser;

import java.sql.Time;
import java.time.LocalTime;

/**
 * Parser for the type {@link Time}.
 */
public class SqlTimeParser implements ValueParser<Time> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Time parse(String value) {
        return Time.valueOf(LocalTime.parse(value));
    }

}
