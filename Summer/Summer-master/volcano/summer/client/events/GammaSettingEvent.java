package volcano.summer.client.events;

import volcano.summer.base.manager.event.Event;

public class GammaSettingEvent extends Event {
	private float gammaSetting;

	public GammaSettingEvent(float gammaSetting) {
		this.gammaSetting = gammaSetting;
	}

	public float getGammaSetting() {
		return this.gammaSetting;
	}

	public void setGammaSetting(float gammaSetting) {
		this.gammaSetting = gammaSetting;
	}
}