package volcano.summer.client.events;

import java.util.Random;

import volcano.summer.base.manager.event.Event;
import volcano.summer.client.util.Location;

public class EventMotion extends Event {

	private Location location;
	public boolean onGround;
	public boolean cancel;

	public EventMotion(Location location, boolean onGround) {
		this.location = location;
		this.onGround = onGround;
	}

	public void setAngles(float[] angles) {
		setAngles(angles[0], angles[1]);
	}

	public void setRandomizedAngles(float[] angles) {
		setAngles((float) (angles[0] + (new Random().nextBoolean() ? Math.random() : -Math.random())),
				(float) (angles[1] + (new Random().nextBoolean() ? Math.random() : -Math.random())));
	}

	public void setAngles(float yaw, float pitch) {
		this.location.setYaw(yaw).setPitch(pitch);
	}

	public boolean isOnGround() {
		return onGround;
	}

	public Location getLocation() {
		return location;
	}

	public boolean isCancelled() {
		return cancel;
	}
}
