package com.srnjak.jpa.pagination.rsql;

import com.github.tennaito.rsql.misc.ArgumentFormatException;
import com.github.tennaito.rsql.misc.ArgumentParser;
import com.srnjak.jpa.pagination.rsql.parser.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * Default argument parser implementation.
 */
public class DefaultArgumentParser implements ArgumentParser {

    private final Map<Class<?>, ValueParser<?>> parsers = new HashMap<>();
    {
        parsers.put(String.class, new StringParser());
        parsers.put(Integer.class, new IntegerParser());
        parsers.put(int.class, new IntegerParser());
        parsers.put(Boolean.class, new BooleanParser());
        parsers.put(boolean.class, new BooleanParser());
        parsers.put(Float.class, new FloatParser());
        parsers.put(float.class, new FloatParser());
        parsers.put(Double.class, new DoubleParser());
        parsers.put(double.class, new DoubleParser());
        parsers.put(Long.class, new LongParser());
        parsers.put(long.class, new LongParser());
        parsers.put(BigInteger.class, new BigIntegerParser());
        parsers.put(BigDecimal.class, new BigDecimalParser());
        parsers.put(Date.class, new DateParser());
        parsers.put(Calendar.class, new CalendarParser());
        parsers.put(java.sql.Date.class, new SqlDateParser());
        parsers.put(java.sql.Time.class, new SqlTimeParser());
        parsers.put(java.sql.Timestamp.class, new SqlTimestampParser());
        parsers.put(LocalDate.class, new LocalDateParser());
        parsers.put(LocalTime.class, new LocalTimeParser());
        parsers.put(LocalDateTime.class, new LocalDateTimeParser());
        parsers.put(Instant.class, new InstantParser());
    }

    public DefaultArgumentParser withParsers(ValueParser<?>... customParsers) {

        Arrays.stream(customParsers)
                .forEach(c -> {
                    try {
                        var type = c.getClass()
                                .getMethod("parse", String.class)
                                .getReturnType();

                        parsers.put(type, c);

                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                });

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T parse(String argument, Class<T> type)
            throws
            ArgumentFormatException,
            IllegalArgumentException {

        // Nullable object
        if (argument == null || "null".equalsIgnoreCase(argument.trim())) {
            return null;
        }

        var parser = parsers.get(type);

        try {
            if (parser == null) {
                //noinspection unchecked,rawtypes
                return (T) Optional.of(type)
                        .filter(Class::isEnum)
                        .map(t -> Enum.valueOf((Class<Enum>) t, argument))
                        .orElseThrow(() ->
                                new IllegalArgumentException(argument));
            }

            //noinspection unchecked
            return (T) parser.parse(argument);
        } catch (RuntimeException e) {
            throw new ArgumentFormatException(argument, type);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> List<T> parse(List<String> arguments, Class<T> type)
            throws
            ArgumentFormatException,
            IllegalArgumentException {

        var castedArguments = new ArrayList<T>(arguments.size());
        arguments.forEach(a -> castedArguments.add(this.parse(a, type)));
        return castedArguments;
    }
}
