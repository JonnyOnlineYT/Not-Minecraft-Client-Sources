package volcano.summer.client.modules.misc;

import volcano.summer.base.Summer;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;

public class Weather extends Module {

	public Weather() {
		super("Weather", 0, Category.MISC);
	}

	@Override
	public void onEnable() {
		Summer.tellPlayer("AKA No rain");
	}

	@Override
	public void onDisable() {

	}

	@Override
	public void onEvent(Event event) {

	}

}
