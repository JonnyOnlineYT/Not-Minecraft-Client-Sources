package ru.wendoxd.modules.impl.player;

import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.MultiSelectBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class NoDelay extends Module {

	private Frame nodelay_frame = new Frame("NoDelay");
	private final CheckBox nodelay = new CheckBox("NoDelay").markArrayList("NoDelay");
	private final MultiSelectBox type = new MultiSelectBox("Type", new String[] { "Click", "Jump", "BlockHit" },
			() -> nodelay.isEnabled(true));

	@Override
	protected void initSettings() {
		nodelay.markSetting("NoDelay");
		nodelay_frame.register(nodelay, type);
		MenuAPI.player.register(nodelay_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventUpdate && nodelay.isEnabled(false)) {
			if (type.get(0)) {
				mc.rightClickDelayTimer = 0;
			}

			if (type.get(0)) {
				mc.leftClickCounter = 0;
			}

			if (type.get(1)) {
				mc.player.jumpTicks = 0;
			}

			if (type.get(2)) {
				mc.playerController.blockHitDelay = 0;
			}
		}
	}
}
