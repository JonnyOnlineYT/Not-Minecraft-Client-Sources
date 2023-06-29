package ru.wendoxd.modules.impl.combat;

import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventBlockDistance;
import ru.wendoxd.events.impl.player.EventEntityDistanceAttack;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class Reach extends Module {

	private Frame reach_frame = new Frame("Reach");
	private final CheckBox reach = new CheckBox("Reach").markArrayList("Reach");
	private final CheckBox entityreach = new CheckBox("Entity", () -> reach.isEnabled(false));
	private final CheckBox blockreach = new CheckBox("Block", () -> reach.isEnabled(false));
	private final Slider size_entity = new Slider("Entity Dist", 1, 3, 6, 0,
			() -> reach.isEnabled(true) && entityreach.isEnabled(true));
	private final Slider size_block = new Slider("Block Dist", 1, 3, 6, 0,
			() -> reach.isEnabled(true) && blockreach.isEnabled(true));

	@Override
	protected void initSettings() {
		reach.markSetting("Reach");
		reach_frame.register(reach, entityreach, size_entity, blockreach, size_block);
		MenuAPI.combat.register(reach_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (reach.isEnabled(false)) {
			if (event instanceof EventEntityDistanceAttack && entityreach.isEnabled(false)) {
				((EventEntityDistanceAttack) event).setDistance(size_entity.getFloatValue());
			}
			if (event instanceof EventBlockDistance && blockreach.isEnabled(false)) {
				((EventBlockDistance) event).setDistance(size_block.getFloatValue());
			}
		}
	}
}
