package com.srnjak.jpa.pagination;

import java.util.HashMap;
import java.util.Map;

/**
 * Direction where to search (i.e. in keyset pagination).
 */
public enum Direction {
    BACKWARD,
    FORWARD;

    private static final Map<Direction, Direction> OPOSITE_MAP = new HashMap<>();
    static {
        OPOSITE_MAP.put(BACKWARD, FORWARD);
        OPOSITE_MAP.put(FORWARD, BACKWARD);
    }

    public Direction oposite() {
        return OPOSITE_MAP.get(this);
    }
}
