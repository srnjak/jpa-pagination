package com.srnjak.jpa.pagination.rsql.parser;

import com.srnjak.jpa.pagination.rsql.ValueParser;

/**
 * Parser for the type {@link Boolean}.
 */
public class BooleanParser implements ValueParser<Boolean> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean parse(String value) {
        return Boolean.valueOf(value);
    }

}
