package splash.client.events.player;

import me.hippo.systems.lwjeb.event.MultiStage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import splash.Splash;
import splash.client.modules.movement.Flight;
import splash.client.modules.movement.Speed;

public class EventPlayerUpdate extends MultiStage {
	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;
	private boolean ground;
	private float lastYaw;
	private float lastPitch;
	public EventPlayerUpdate(double x, double y, double z, float yaw, float pitch, boolean ground) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.ground = ground;
	}

	public double getX() {
		return this.x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return this.y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return this.z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public float getYaw() {
		return this.yaw;
	}
	
	public float getLastYaw() {
		return this.lastYaw;
	}
	
	public float getLastPitch() {
		return this.lastPitch;
	}
	
	public void setLastYaw(float yaw) {
		this.lastYaw = yaw;
	}
	
	public void setLastPitch(float pitch) {
		this.lastPitch = pitch;
	}
	
	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getPitch() {
		return this.pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public boolean getGroundState() {
		return this.ground;
	}

	public void setGround(boolean ground) {
		this.ground = ground;
	}

}
