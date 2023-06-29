package volcano.summer.client.modules.player;

import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventTick;

public class SpeedMine extends Module {

	public SpeedMine() {
		super("SpeedMine", 0, Category.PLAYER);
	}

	@Override
	public void onEnable() {

	}

	@Override
	public void onDisable() {

	}

	@Override
	public void onEvent(Event event) {
		if (mc.theWorld != null) {
			if (event instanceof EventTick) {
				if (this.mc.playerController.curBlockDamageMP > 0.8F) {
					this.mc.playerController.curBlockDamageMP = 1.0F;
				}
				this.mc.playerController.blockHitDelay = 0;
			}
		}
	}
}
