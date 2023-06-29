package ru.wendoxd.events.impl.menu;

import ru.wendoxd.events.Event;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;

public class EventSwapState extends Event {

	private CheckBox checkBox;
	private boolean state;

	public EventSwapState(CheckBox checkBox, boolean state) {
		this.checkBox = checkBox;
		this.state = state;
	}

	public CheckBox getCheckBox() {
		return this.checkBox;
	}

	public boolean getState() {
		return this.state;
	}

	public void setState(boolean state) {
		this.state = state;
	}
}
