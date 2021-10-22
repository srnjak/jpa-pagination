package com.srnjak.jpa.pagination.rsql.parser;

import com.srnjak.jpa.pagination.rsql.ValueParser;

/**
 * Parser for the type {@link Double}.
 */
public class DoubleParser implements ValueParser<Double> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Double parse(String value) {
        return Double.valueOf(value);
    }

}
