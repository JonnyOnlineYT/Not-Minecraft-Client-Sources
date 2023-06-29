package org.moonware.client.feature.impl.visual.anim;

public enum Direction {
    FORWARDS,
    BACKWARDS;

    public Direction opposite() {
        if (this == FORWARDS) {
            return BACKWARDS;
        } else return FORWARDS;
    }

}
