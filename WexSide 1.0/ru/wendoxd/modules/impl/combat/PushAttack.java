package ru.wendoxd.modules.impl.combat;

import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class PushAttack extends Module {

	private Frame pushattack_frame = new Frame("PushAttack");
	private final CheckBox pushattack = new CheckBox("PushAttack").markArrayList("PushAttack");

	@Override
	protected void initSettings() {
		pushattack.markSetting("PushAttack");
		pushattack_frame.register(pushattack);
		MenuAPI.combat.register(pushattack_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventUpdate && pushattack.isEnabled(false)) {
			if (mc.player.getCooledAttackStrength(0) == 1 && mc.gameSettings.keyBindAttack.pressed) {
				mc.clickMouse();
			}
		}
	}
}
