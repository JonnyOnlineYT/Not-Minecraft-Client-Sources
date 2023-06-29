package volcano.summer.client.modules.movement;

import org.lwjgl.input.Keyboard;

import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventUpdate;

public class Sprint extends Module {

	public Sprint() {
		super("Sprint", 0, Category.MOVEMENT);
	}

	@Override
	public void onEnable() {
		
	}

	@Override
	public void onDisable() {
		mc.thePlayer.setSprinting(false);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventUpdate) {
			if ((this.mc.thePlayer.moveStrafing != 0.0F) || ((this.mc.thePlayer.moveForward != 0.0F) && ((!this.mc.thePlayer.isOnLadder()) || (!this.mc.thePlayer.isSneaking()) || (!this.mc.thePlayer.isInWater())))) {
				mc.thePlayer.setSprinting(true);
			}
		}
	}
}
