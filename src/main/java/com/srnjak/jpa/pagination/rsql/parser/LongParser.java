package com.srnjak.jpa.pagination.rsql.parser;

import com.srnjak.jpa.pagination.rsql.ValueParser;

/**
 * Parser for the type {@link Long}.
 */
public class LongParser implements ValueParser<Long> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Long parse(String value) {
        return Long.valueOf(value);
    }

}
