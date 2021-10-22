package com.srnjak.jpa.pagination.rsql.parser;

import com.srnjak.jpa.pagination.rsql.ValueParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * Parser for the type {@link Date}.
 */
public class DateParser implements ValueParser<Date> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Date parse(String value) {

        try {
            return Date.from(ZonedDateTime.parse(value).toInstant());
        } catch (DateTimeParseException e) {
            // Not a zoned date time format, lets try with date format.
        }

        try {
            return Date.from(LocalDateTime.parse(value)
                    .atZone(ZoneOffset.UTC)
                    .toInstant());
        } catch (DateTimeParseException e1) {
            // Not a date time format, lets try with date format.
        }

        return Date.from(LocalDate.parse(value)
                .atTime(0, 0, 0)
                .atZone(ZoneOffset.UTC)
                .toInstant());
    }

}
