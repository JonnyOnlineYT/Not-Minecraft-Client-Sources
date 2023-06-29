package volcano.summer.client.util;

public class OBTimer {

	private long lastMS;

	public long getCurrentMS() {
		return System.nanoTime() / 1000000L;
	}

	public long getLastMS() {
		return lastMS;
	}

	public boolean hasReached(long milliseconds) {
		return getCurrentMS() - lastMS >= milliseconds;
	}

	public long getTime() {
		return getCurrentMS() - lastMS;
	}

	public void reset() {
		lastMS = getCurrentMS();
	}

	public void setLastMS(long currentMS) {
		lastMS = currentMS;
	}

}
