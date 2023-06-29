package ru.wendoxd.modules.impl.combat;

import ru.wendoxd.events.Event;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.SelectBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class AntiBot extends Module {
	public static Frame antibot_frame = new Frame("AntiBot");
	public static CheckBox antibot = new CheckBox("AntiBot").markArrayList("AntiBot");
	public static SelectBox mode = new SelectBox("Mode", new String[] { "Matrix V" }, () -> antibot.isEnabled(true));
	public static CheckBox removeBot = new CheckBox("Remove bot", () -> antibot.isEnabled(true));

	@Override
	protected void initSettings() {
		antibot.markSetting("AntiBot");
		mode.modifyPath("Mode");
		antibot_frame.register(antibot, mode, removeBot);
		MenuAPI.combat.register(antibot_frame);
	}

	@Override
	public void onEvent(Event event) {
	}
}
