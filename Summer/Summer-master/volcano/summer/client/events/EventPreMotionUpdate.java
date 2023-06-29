package volcano.summer.client.events;

import java.util.Random;

import volcano.summer.base.manager.event.Event;

public class EventPreMotionUpdate extends Event {

	private boolean cancel;

	public float yaw, pitch;

	private boolean ground;

	public double y;

	public EventPreMotionUpdate(float yaw, float pitch, boolean ground, double y) {
		this.yaw = yaw;
		this.pitch = pitch;
		this.ground = ground;
		this.y = y;
	}

	public boolean isCancel() {

		return cancel;
	}

	public void setCancel(boolean cancel) {

		this.cancel = cancel;
	}

	public float getYaw() {

		return yaw;
	}

	public void setYaw(float yaw) {

		this.yaw = yaw;
	}

	public float getPitch() {

		return pitch;
	}

	public void setPitch(float pitch) {

		this.pitch = pitch;
	}

	public double getY() {

		return y;
	}

	public void setY(double y) {

		this.y = y;
	}

	public void setGround(boolean ground) {
		this.ground = ground;
	}

	public void setServerAngles(float[] angles, boolean randomize) {
		if (angles[0] == -999 && angles[1] == -999)
			return;
		this.yaw = (float) (angles[0] + (randomize ? new Random().nextBoolean() ? Math.random() : -Math.random() : 0));
		this.pitch = (float) (angles[1] + (randomize
				? new Random().nextBoolean() ? (new Random().nextInt(6) + Math.random()) : -(new Random().nextInt(6))
				: 0));

	}
}
