package com.srnjak.jpa.pagination.rsql.parser;

import com.srnjak.jpa.pagination.rsql.ValueParser;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Parser for the type {@link Date}.
 */
public class SqlDateParser implements ValueParser<Date> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Date parse(String value) {
        return Date.valueOf(LocalDate.parse(value));
    }

}
