package com.srnjak.jpa.pagination.rsql.parser;

import com.srnjak.jpa.pagination.rsql.ValueParser;

/**
 * Parser for the type {@code int}.
 */
public class IntParser implements ValueParser<Integer> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer parse(String value) {
        return Integer.valueOf(value);
    }

}
