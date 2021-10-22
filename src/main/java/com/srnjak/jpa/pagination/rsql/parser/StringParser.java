package com.srnjak.jpa.pagination.rsql.parser;

import com.srnjak.jpa.pagination.rsql.ValueParser;

/**
 * Parser for the type {@link String}.
 */
public class StringParser implements ValueParser<String> {

    /**
     * {@inheritDoc}
     */
    @Override
    public String parse(String value) {
        return value;
    }

}
