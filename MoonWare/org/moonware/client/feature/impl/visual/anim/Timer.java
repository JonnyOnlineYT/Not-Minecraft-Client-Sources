package org.moonware.client.feature.impl.visual.anim;

public class Timer {

    private long lastMS = -1L;

    public Timer() {
        lastMS = System.currentTimeMillis();
    }

    public boolean hasReached(double delay) {
        return System.currentTimeMillis() - lastMS >= delay;
    }

    public boolean hasReached(boolean active, double delay) {
        return active || hasReached(delay);
    }

    public long getLastMS() {
        return lastMS;
    }

    public void reset() {
        lastMS = System.currentTimeMillis();
    }

    public long getTimePassed() {
        return System.currentTimeMillis() - lastMS;
    }

    public long getCurrentTime() {
        return System.nanoTime() / 1000000L;
    }

    public void setTime(long time) {
        lastMS = time;
    }

}
