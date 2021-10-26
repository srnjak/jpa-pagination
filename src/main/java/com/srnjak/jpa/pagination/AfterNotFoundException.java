package com.srnjak.jpa.pagination;

import lombok.Getter;

/**
 * The exception class for case when 'after' value cannot be obtained.
 */
public class AfterNotFoundException extends RuntimeException{

    /**
     * The id of the 'after' object.
     */
    @Getter
    private final Object id;

    /**
     * Constructor.
     *
     * @param id The id of the 'after' object.
     */
    public AfterNotFoundException(Object id) {
        super("id=" + id);

        this.id = id;
    }
}
