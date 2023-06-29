package Celestial.utils.otherutils.gayutil;

public class Counter {
    private long lastMS;

    public Counter() {
    }

    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public long getLastMS() {
        return this.lastMS;
    }

    public long getValue() {
        return this.getCurrentMS() - this.lastMS;
    }

    public boolean hasReached(double milliseconds) {
        return (double)(this.getCurrentMS() - this.lastMS) >= milliseconds;
    }

    public boolean hasReached(int milliseconds) {
        return this.getCurrentMS() - this.lastMS >= (long)milliseconds;
    }

    public boolean hasReached(long milliseconds) {
        return this.getCurrentMS() - this.lastMS >= milliseconds;
    }

    public boolean hasReached(float milliseconds) {
        return (float)(this.getCurrentMS() - this.lastMS) >= milliseconds;
    }

    public boolean hasReached(Float milliseconds) {
        return (float)(this.getCurrentMS() - this.lastMS) >= milliseconds;
    }

    public void reset() {
        this.lastMS = this.getCurrentMS();
    }

    public void setLastMS(long currentMS) {
        this.lastMS = currentMS;
    }
}

