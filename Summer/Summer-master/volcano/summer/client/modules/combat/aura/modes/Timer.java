package volcano.summer.client.modules.combat.aura.modes;

public class Timer {

	private long lastTime;

	public Timer() {
		reset();
	}

	public boolean hasReached(double delay) {
		return ((System.currentTimeMillis() - lastTime) >= delay);
	}

	public Timer reset() {
		this.lastTime = System.currentTimeMillis();
		return this;
	}

	public long getLastTime() {
		return lastTime;
	}
}