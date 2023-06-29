package org.moonware.client.helpers.Utils;

public enum Direction {
    FORWARDS,
    BACKWARDS;

    public Direction opposite() {
        if (this == FORWARDS) {
            return BACKWARDS;
        } else return FORWARDS;
    }

}
