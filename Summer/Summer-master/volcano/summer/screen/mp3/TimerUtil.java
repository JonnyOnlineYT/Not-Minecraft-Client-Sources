package volcano.summer.screen.mp3;

public class TimerUtil {
	private long prevMS;

	public TimerUtil() {
		this.prevMS = 0L;
	}

	public boolean delay(final double d) {
		if (this.getTime() - this.getPrevMS() >= d) {
			this.reset();
			return true;
		}
		return false;
	}

	public void reset() {
		this.prevMS = this.getTime();
	}

	public long getTime() {
		return System.nanoTime() / 1000000L;
	}

	public long getPrevMS() {
		return this.prevMS;
	}
}
