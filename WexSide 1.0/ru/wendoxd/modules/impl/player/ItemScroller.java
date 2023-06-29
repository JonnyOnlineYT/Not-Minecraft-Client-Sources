package ru.wendoxd.modules.impl.player;

import ru.wendoxd.events.Event;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class ItemScroller extends Module {

	private Frame itemscroller_frame = new Frame("ItemScroller");
	public static CheckBox itemscroller = new CheckBox("ItemScroller").markArrayList("ItemScroller");
	public static Slider delay = new Slider("Delay", 1, 1, 400, 0.24, () -> itemscroller.isEnabled(true));

	@Override
	protected void initSettings() {
		itemscroller.markSetting("ItemScroller");
		delay.modifyPath("Delay_4");
		itemscroller_frame.register(itemscroller, delay);
		MenuAPI.player.register(itemscroller_frame);
	}

	@Override
	public void onEvent(Event event) {

	}
}
