package ru.rockstar.api.utils.world;

public class TimerHelper {
    private long prevMS;
    private long lastMS;
    public static long previousTime = -1L;
    public static float timerSpeed = 1.0F;
    private long tick = 0;

    public TimerHelper() {
        this.prevMS = 0L;
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }
    public boolean check(float milliseconds) {
        return (float) (getCurrentTime() - this.previousTime) >= milliseconds;
    }


    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public boolean hasTimeElapsed(long time, boolean reset) {
        if (System.currentTimeMillis() - lastMS > time) {
            if (reset)
                reset();


            return true;
        }

        return false;
    }

    public long getDifference() {
        return getTime() - this.prevMS;
    }

    public void setDifference(long difference) {
        this.prevMS = (getTime() - difference);
    }

    public boolean hasReached(double milliseconds) {
        return (double)(this.getCurrentMS() - this.lastMS) >= milliseconds;
    }
    public boolean hasTimePassed(float ticks) {
        return tick >= ticks;
    }

    public void resetwatermark() {
        this.previousTime = getCurrentTime();
    }

    public void reset() {
        this.lastMS = this.getCurrentMS();
    }

    public boolean delay(float milliSec) {
        return (float) (getTime() - this.prevMS) >= milliSec;
    }

    public void setTime(long time) {
        this.lastMS = time;
    }

    public long getTime() {
        return this.getCurrentMS() - this.lastMS;
    }
}