package ru.wendoxd.modules.impl.visuals;

import ru.wendoxd.events.Event;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class EnchantmentColor extends Module {

	private Frame enchantmentcolor_frame = new Frame("EnchantmentColor");
	public static CheckBox enchantmentcolor = new CheckBox("EnchantmentColor");

	@Override
	protected void initSettings() {
		enchantmentcolor_frame.register(enchantmentcolor.markSetting("EnchantmentColor").markColorPicker());
		MenuAPI.hud.register(enchantmentcolor_frame);
	}

	@Override
	public void onEvent(Event event) {

	}
}
