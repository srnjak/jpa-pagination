package com.srnjak.jpa.pagination.rsql.parser;

import com.srnjak.jpa.pagination.rsql.ValueParser;

import java.math.BigDecimal;

/**
 * Parser for the type {@link BigDecimal}.
 */
public class BigDecimalParser implements ValueParser<BigDecimal> {

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal parse(String value) {
        return new BigDecimal(value);
    }

}
