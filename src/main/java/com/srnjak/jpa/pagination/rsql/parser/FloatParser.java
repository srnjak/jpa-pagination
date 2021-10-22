package com.srnjak.jpa.pagination.rsql.parser;

import com.srnjak.jpa.pagination.rsql.ValueParser;

/**
 * Parser for the type {@link Float}.
 */
public class FloatParser implements ValueParser<Float> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Float parse(String value) {
        return Float.valueOf(value);
    }

}
