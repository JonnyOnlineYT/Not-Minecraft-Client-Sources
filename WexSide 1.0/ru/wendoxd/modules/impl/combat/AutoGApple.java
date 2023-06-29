package ru.wendoxd.modules.impl.combat;

import net.minecraft.item.ItemAppleGold;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class AutoGApple extends Module {

	private Frame autogapple_frame = new Frame("AutoGApple");
	private final CheckBox autogapple = new CheckBox("AutoGApple").markArrayList("AutoGApple");
	private final Slider health = new Slider("Health", 1, 1, 20, 0.5, () -> autogapple.isEnabled(true));
	private boolean isActive;

	@Override
	protected void initSettings() {
		autogapple_frame.register(autogapple.markSetting("AutoGApple"), health);
		MenuAPI.combat.register(autogapple_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventUpdate && autogapple.isEnabled(false)) {
			if (mc.player.getHeldItemOffhand().getItem() instanceof ItemAppleGold
					&& mc.player.getHealth() <= health.getFloatValue()) {
				isActive = true;
				mc.gameSettings.keyBindUseItem.pressed = true;
			} else if (isActive) {
				mc.gameSettings.keyBindUseItem.pressed = false;
				isActive = false;
			}
		}
	}
}
