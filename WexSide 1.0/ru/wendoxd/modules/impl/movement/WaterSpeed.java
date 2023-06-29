package ru.wendoxd.modules.impl.movement;

import net.minecraft.init.MobEffects;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.player.MoveUtils;

public class WaterSpeed extends Module {

	private Frame waterspeed_frame = new Frame("WaterSpeed");
	private final CheckBox waterspeed = new CheckBox("WaterSpeed").markArrayList("WaterSpeed");
	private final Slider speed = new Slider("Speed", 2, 0.1, 2, 0.2, () -> waterspeed.isEnabled(true));
	private final CheckBox checkspeedpotion = new CheckBox("Only Speed Potion", () -> waterspeed.isEnabled(true));

	@Override
	protected void initSettings() {
		waterspeed.markSetting("WaterSpeed");
		speed.modifyPath("Speed_2");
		waterspeed_frame.register(waterspeed, speed, checkspeedpotion);
		MenuAPI.movement.register(waterspeed_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventUpdate && waterspeed.isEnabled(false) && mc.player.isInWater()) {
			if (checkspeedpotion.isEnabled(false) && !mc.player.isPotionActive(MobEffects.SPEED))
				return;

			MoveUtils.setSpeed(speed.getFloatValue());
		}
	}
}