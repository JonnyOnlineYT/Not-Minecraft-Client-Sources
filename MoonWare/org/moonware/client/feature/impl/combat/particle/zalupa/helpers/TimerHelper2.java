package org.moonware.client.feature.impl.combat.particle.zalupa.helpers;

public class TimerHelper2 {
    public long lastMS;
    private long time;

    private long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }
    private long prevTime;
    public boolean hasReached(double milliseconds) {
        return (double) (getCurrentMS() - lastMS) >= milliseconds;
    }
    public void setTime(long time) {
        lastMS = time;
    }
    public boolean hasTimeElapsed(long time) {
        return System.currentTimeMillis() - lastMS > time;
    }
    public boolean hasPassed(double milli) {
        return System.currentTimeMillis() - prevTime >= milli;
    }
    public boolean sleep(long time) {
        if (time() >= time) {
            reset();
            return true;
        }
        return false;
    }
    public long time() {
        return System.nanoTime() / 1000000L - time;
    }
    public final long getElapsedTime() {
        return getCurrentMS() - lastMS;
    }

    public void reset() {
        lastMS = getCurrentMS();
    }

    public boolean delay(float milliSec) {
        return (float) (getTime() - lastMS) >= milliSec;
    }

    public long getTime() {
        return System.nanoTime() / 1000000L;
    }

    public boolean isDelayComplete(long delay) {
        return System.currentTimeMillis() - lastMS > delay;
    }
}
