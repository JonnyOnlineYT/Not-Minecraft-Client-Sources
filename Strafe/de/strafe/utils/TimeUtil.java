package de.strafe.utils;

public class TimeUtil {

    private long lastMS = 0;

    public boolean hasReached(long ms) {
        return System.currentTimeMillis() - this.lastMS >= ms;
    }

    public void reset() {
        this.lastMS = System.currentTimeMillis();
    }

}
