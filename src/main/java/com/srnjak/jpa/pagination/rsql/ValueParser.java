package com.srnjak.jpa.pagination.rsql;

public interface ValueParser<T> {

    T parse(String value);
}
