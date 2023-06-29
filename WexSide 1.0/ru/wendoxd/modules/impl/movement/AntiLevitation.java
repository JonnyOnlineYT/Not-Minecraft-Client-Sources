package ru.wendoxd.modules.impl.movement;

import net.minecraft.init.MobEffects;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class AntiLevitation extends Module {

	private Frame antilevitation_frame = new Frame("AntiLevitation");
	private final CheckBox antilevitation = (CheckBox) new CheckBox("AntiLevitation").markArrayList("AntiLevitation")
			.markDescription("Убирает зелье левитации");

	@Override
	protected void initSettings() {
		antilevitation.markSetting("AntiLevitation");
		antilevitation_frame.register(antilevitation);
		MenuAPI.movement.register(antilevitation_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventUpdate && antilevitation.isEnabled(false)) {
			if (mc.player.isPotionActive(MobEffects.LEVITATION)) {
				mc.player.removeActivePotionEffect(MobEffects.LEVITATION);
			}
		}
	}
}
