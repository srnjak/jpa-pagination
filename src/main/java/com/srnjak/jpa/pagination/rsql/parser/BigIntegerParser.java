package com.srnjak.jpa.pagination.rsql.parser;

import com.srnjak.jpa.pagination.rsql.ValueParser;

import java.math.BigInteger;

/**
 * Parser for the type {@link BigInteger}.
 */
public class BigIntegerParser implements ValueParser<BigInteger> {

    /**
     * {@inheritDoc}
     */
    @Override
    public BigInteger parse(String value) {
        return new BigInteger(value);
    }

}
