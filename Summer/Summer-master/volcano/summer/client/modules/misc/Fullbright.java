package volcano.summer.client.modules.misc;

import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;

public class Fullbright extends Module {

	float oldGammaSetting;

	public Fullbright() {
		super("Fullbright", 0, Category.MISC);
	}

	@Override
	public void onEnable() {
		this.oldGammaSetting = this.mc.gameSettings.gammaSetting;
		this.mc.gameSettings.gammaSetting = 1000.0F;
		this.mc.renderGlobal.loadRenderers();
	}

	@Override
	public void onDisable() {
		this.mc.gameSettings.gammaSetting = this.oldGammaSetting;
		this.mc.renderGlobal.loadRenderers();
	}

	@Override
	public void onEvent(Event event) {
	}
}
