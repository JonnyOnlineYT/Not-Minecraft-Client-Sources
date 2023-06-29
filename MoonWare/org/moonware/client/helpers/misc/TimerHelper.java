package org.moonware.client.helpers.misc;

import org.moonware.client.helpers.Helper;

public class TimerHelper implements Helper {

    private long ms = getCurrentMS();

    private long getCurrentMS() {
        return System.currentTimeMillis();
    }

    public boolean hasReached(float milliseconds) {
        return getCurrentMS() - ms > milliseconds;
    }
    public boolean hasReached(double milliseconds) {
        return (double)(getCurrentMS() - ms) > milliseconds;
    }

    public void reset() {
        ms = getCurrentMS();
    }

    public long getTime() {
        return getCurrentMS() - ms;
    }
}
