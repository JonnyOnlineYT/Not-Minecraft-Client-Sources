package ua.apraxia.utility.math;


import ua.apraxia.utility.Utility;

public class TimerUtility implements Utility {
    private long ms = getCurrentMS();

    private long getCurrentMS() {
        return System.currentTimeMillis();
    }

    public boolean hasReached(double milliseconds) {
        return ((getCurrentMS() - this.ms) > milliseconds);
    }

    public void reset() {
        this.ms = getCurrentMS();
    }

    public long getTime() {
        return getCurrentMS() - this.ms;
    }
}

