package ru.wendoxd.modules.impl.movement;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class GuiWalk extends Module {

	private Frame guiwalk_frame = new Frame("GuiWalk");
	private final CheckBox guiwalk = new CheckBox("GuiWalk").markArrayList("GuiWalk");

	@Override
	protected void initSettings() {
		guiwalk.markSetting("GuiWalk");
		guiwalk_frame.register(guiwalk);
		MenuAPI.movement.register(guiwalk_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventUpdate && guiwalk.isEnabled(false)) {

			KeyBinding[] keys = { mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack,
					mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindJump,
					mc.gameSettings.keyBindSprint };

			if (mc.currentScreen instanceof GuiChat || mc.currentScreen instanceof GuiEditSign)
				return;

			for (KeyBinding keyBinding : keys) {
				keyBinding.pressed = Keyboard.isKeyDown(keyBinding.getKeyCode());
			}
		}
	}
}
