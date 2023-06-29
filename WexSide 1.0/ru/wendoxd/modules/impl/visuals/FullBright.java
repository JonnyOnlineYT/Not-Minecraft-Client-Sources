package ru.wendoxd.modules.impl.visuals;

import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.menu.EventSwapState;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class FullBright extends Module {

	private Frame fullbright_frame = new Frame("FullBright");
	private final CheckBox fullbright = new CheckBox("FullBright").markArrayList("FullBright");

	@Override
	protected void initSettings() {
		fullbright.markSetting("FullBright");
		fullbright_frame.register(fullbright);
		MenuAPI.visuals.register(fullbright_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventUpdate && fullbright.isEnabled(false)) {
			mc.player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 1337, 1));
		}
		if (event instanceof EventSwapState) {
			if (((EventSwapState) event).getCheckBox() == fullbright) {
				if (!((EventSwapState) event).getState()) {
					mc.player.removePotionEffect(MobEffects.NIGHT_VISION);
				}
			}
		}
	}
}
