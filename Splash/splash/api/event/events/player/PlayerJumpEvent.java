package splash.api.event.events.player;

import me.hippo.systems.lwjeb.event.Cancelable;

public class PlayerJumpEvent extends Cancelable {
	public double motionY;
	public PlayerJumpEvent(double motionY) {
		this.motionY = motionY;
	}
	
	public void setMotionY(double d) {
		this.motionY = d;
	}
	
	public double getMotionY() {
		return motionY;
	}
}
