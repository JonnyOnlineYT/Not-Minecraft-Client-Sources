package volcano.summer.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import volcano.summer.base.manager.event.Event;
import volcano.summer.client.util.Location;

public class EventMove extends Event {

	public static double x;
	public static double y;
	public static double z;
	private Location location;

	public EventMove(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public EventMove(Location location) {
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}

	public void setMovementSpeed(double moveSpeed) {
		Minecraft mc = Minecraft.getMinecraft();
		moveSpeed = (float) moveSpeed;
		float forward = mc.thePlayer.movementInput.moveForward;
		float strafe = mc.thePlayer.movementInput.moveStrafe;
		float yaw = mc.thePlayer.rotationYaw;
		if ((forward == 0.0F) && (strafe == 0.0F)) {
			multiply(new Location(0, 1, 0));
		} else if (forward != 0.0F) {
			if (strafe >= 1.0F) {
				yaw += (forward > 0.0F ? -45 : 45);
				strafe = 0.0F;
			} else if (strafe <= -1.0F) {
				yaw += (forward > 0.0F ? 45 : -45);
				strafe = 0.0F;
			}
			if (forward > 0.0F) {
				forward = 1.0F;
			} else if (forward < 0.0F) {
				forward = -1.0F;
			}
		}
		double mx = Math.cos(Math.toRadians(yaw + 90.0F));
		double mz = Math.sin(Math.toRadians(yaw + 90.0F));
		this.setX((float) (forward * moveSpeed * mx + strafe * moveSpeed * mz));
		this.setZ((float) (forward * moveSpeed * mz - strafe * moveSpeed * mx));
		mc.thePlayer.stepHeight = 0.6F;
	}

	public static void multiply(Location location) {
		x *= location.getX();
		y *= location.getY();
		z *= location.getZ();
	}

	/**
	 * The default player walking speed.
	 */
	private static final double WALK_SPEED = 0.21585;

	/**
	 * The default player running speed.
	 */
	private static final double RUN_SPEED = 0.2806;

	/**
	 * @return the base movement speed of the player without potions.
	 */
	private double getBaseMovementSpeed() {
		return Minecraft.getMinecraft().thePlayer.isSprinting() ? RUN_SPEED : WALK_SPEED;
	}

	/**
	 * @return the player's movement speed with it's potion effects.
	 */
	public double getMovementSpeed() {
		if (!Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed))
			return getBaseMovementSpeed();
		return getBaseMovementSpeed() * 1 + (0.06
				* (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1));
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	protected boolean cancelled;

	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setCancelled(boolean state) {
		this.cancelled = state;
	}
}
