package com.srnjak.jpa.pagination.rsql.parser;

import com.srnjak.jpa.pagination.rsql.ValueParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Parser for the type {@link Calendar}.
 */
public class CalendarParser implements ValueParser<Calendar> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Calendar parse(String value) {

        ZonedDateTime zdt;
        try {
            zdt = ZonedDateTime.parse(value);
        } catch (DateTimeParseException e) {
            try {
                zdt = LocalDateTime.parse(value)
                        .atZone(ZoneOffset.UTC);
            } catch (DateTimeParseException e1) {
                zdt = LocalDate.parse(value)
                        .atTime(0, 0, 0)
                        .atZone(ZoneOffset.UTC);
            }
        }

        return GregorianCalendar.from(zdt);
    }
}
