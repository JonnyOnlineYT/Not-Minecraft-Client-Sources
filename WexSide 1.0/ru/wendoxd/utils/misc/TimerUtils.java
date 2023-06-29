package ru.wendoxd.utils.misc;

public class TimerUtils {

    public long currentMs = getCurrentMs();

    public long getCurrentMs() {
        return System.currentTimeMillis();
    }

    public boolean hasReached(float ms) {
        return getCurrentMs() - currentMs > ms;
    }

    public void reset() {
        currentMs = getCurrentMs();
    }
}
