package org.spray.heaven.events;

import org.spray.heaven.main.Wrapper;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import com.darkmagician6.eventapi.types.EventType;

/**
 * 
 * Inject into the EntityPlayerSP class
 * 
 * @method = onUpdateWalkingPlayer();
 * @type = PRE, POST
 *
 */
public class MotionEvent extends EventCancellable {

	private EventType type;
	private double posX, posY, posZ;
	private float yaw, pitch;
	private boolean onGround;

	public MotionEvent(EventType type, double posX, double posY, double posZ, float yaw, float pitch,
			boolean onGround) {
		this.type = type;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.yaw = yaw;
		this.pitch = pitch;
		this.onGround = onGround;
	}

	public void setRotation(float yaw, float pitch, boolean clientRotation) {
		if (Float.isNaN(yaw) || Float.isNaN(pitch) || pitch > 90 || pitch < -90)
			return;
		
		this.yaw = yaw;
		this.pitch = pitch;
		
		if (clientRotation) {
			Wrapper.getPlayer().setYaw(yaw);
			Wrapper.getPlayer().setPitch(pitch);
		}
	}

	public EventType getType() {
		return type;
	}

	public double getPosX() {
		return posX;
	}

	public void setPosX(double posX) {
		this.posX = posX;
	}

	public double getPosY() {
		return posY;
	}

	public void setPosY(double posY) {
		this.posY = posY;
	}

	public double getPosZ() {
		return posZ;
	}

	public void setPosZ(double posZ) {
		this.posZ = posZ;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		float f = (float) (Wrapper.MC.gameSettings.mouseSensitivity * 0.6F + 0.2F);
		float gcd = (f * f * f) * 1.2f;

		yaw -= yaw % gcd;

		this.yaw = yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		float f = (float) (Wrapper.MC.gameSettings.mouseSensitivity * 0.6F + 0.2F);
		float gcd = (f * f * f) * 1.2f;

		pitch -= pitch % gcd;

		this.pitch = pitch;
	}

	public boolean isOnGround() {
		return onGround;
	}

	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}
}
