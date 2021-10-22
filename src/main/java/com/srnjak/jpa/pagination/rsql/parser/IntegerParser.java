package com.srnjak.jpa.pagination.rsql.parser;

import com.srnjak.jpa.pagination.rsql.ValueParser;

/**
 * Parser for the type {@link Integer}.
 */
public class IntegerParser implements ValueParser<Integer> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer parse(String value) {
        return Integer.valueOf(value);
    }

}
