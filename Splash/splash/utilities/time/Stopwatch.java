package splash.utilities.time;

/**
 * Author: Ice no cap
 * Created: 17:19, 11-Jun-20
 * Project: Client
 */
public class Stopwatch {
    private long prevMS = 0L;

    public Stopwatch() {
        this.reset();
    }

    public boolean delay(final float milliSec) {
        return this.getTime() - this.prevMS >= milliSec;
    }

    public void reset() {
        this.prevMS = this.getTime();
    }

    public long getTime() {
        return System.nanoTime() / 1000000L;
    }

    public boolean sleep(long time) {
        if (getTime() >= time) {
            reset();
            return true;
        }
        return false;
    }

}