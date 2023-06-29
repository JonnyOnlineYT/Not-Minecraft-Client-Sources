package ru.wendoxd.modules.impl.visuals;

import ru.wendoxd.events.Event;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.SelectBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class ClientSounds extends Module {
	public static Frame frame = new Frame("ClientSounds");
	public static CheckBox clientSounds = new CheckBox("ClientSounds");
	public static SelectBox selectBox = new SelectBox("Sound", new String[] { "Minecraft" },
			() -> clientSounds.isEnabled(true));
	public static Slider pitch = new Slider("Pitch", 1, 0, 2, 0.85, () -> selectBox.get() == 0);
	public static Slider volume = new Slider("Volume", 1, 0, 4, 1, () -> selectBox.get() == 0);

	public void initSettings() {
		frame.register(clientSounds, selectBox, pitch, volume);
		MenuAPI.visuals.register(frame);
	}

	public void onEvent(Event event) {
	}
}
