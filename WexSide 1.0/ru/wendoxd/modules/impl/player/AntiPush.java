package ru.wendoxd.modules.impl.player;

import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.block.EventPushBlock;
import ru.wendoxd.events.impl.block.EventWaterPush;
import ru.wendoxd.events.impl.player.EventEntityCollision;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.MultiSelectBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class AntiPush extends Module {

	private Frame antipush_frame = new Frame("AntiPush");
	private final CheckBox antipush = new CheckBox("AntiPush").markArrayList("AntiPush");
	private final MultiSelectBox entities = new MultiSelectBox("Entity", new String[] { "Entity", "Liquid", "Blocks" },
			() -> antipush.isEnabled(true));

	@Override
	protected void initSettings() {
		antipush.markSetting("AntiPush");
		antipush_frame.register(antipush, entities);
		MenuAPI.player.register(antipush_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (antipush.isEnabled(false)) {
			if (event instanceof EventEntityCollision && entities.get(0)) {
				((EventEntityCollision) event).setCanceled();
			}
			if (event instanceof EventWaterPush && entities.get(1)) {
				((EventWaterPush) event).setCanceled();
			}
			if (event instanceof EventPushBlock && entities.get(2)) {
				((EventPushBlock) event).setCanceled();
			}
		}
	}
}
