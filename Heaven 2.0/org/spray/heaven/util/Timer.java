package org.spray.heaven.util;

public class Timer {

    private long lastMS = -1L;

    public Timer() {
        this.lastMS = System.currentTimeMillis();
    }

    public boolean hasReached(double delay) {
        return System.currentTimeMillis() - this.lastMS >= delay;
    }
    
    public boolean hasReached(boolean active, double delay) {
        return active || hasReached(delay);
    }
    
    public long getLastMS() {
        return lastMS;
    }

    public void reset() {
        this.lastMS = System.currentTimeMillis();
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
