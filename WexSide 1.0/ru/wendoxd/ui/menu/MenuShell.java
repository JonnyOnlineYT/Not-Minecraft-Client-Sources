package ru.wendoxd.ui.menu;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.sound.SoundUtils;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

public class MenuShell extends GuiScreen {

	@Override
	public void initGui() {
		SoundUtils.playSound(1.9f);
		Keyboard.enableRepeatEvents(true);
		final ScaledResolution scaledresolution = new ScaledResolution(this.mc, 2);
		Menu.width = scaledresolution.getScaledWidth();
		Menu.height = scaledresolution.getScaledHeight();
		if (!MenuAPI.initialized) {
			MenuAPI.menuWindow.setXY(Menu.width / 2 - 170, Menu.height / 2 - 110);
			MenuAPI.initialized = true;
		}
	}

	@Override
	public void updateScreen() {
		KeyBinding[] keys = { mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindLeft,
				mc.gameSettings.keyBindRight, mc.gameSettings.keyBindJump, mc.gameSettings.keyBindSprint };
		try {
			for (KeyBinding keyBinding : keys) {
				keyBinding.pressed = Keyboard.isKeyDown(keyBinding.getKeyCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void mouseClicked(int x, int y, int mb) {
		Menu.mouseClicked(MenuAPI.mouseX, MenuAPI.mouseY, mb);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == 1) {
			this.mc.displayGuiScreen(null);
			if (this.mc.currentScreen == null) {
				this.mc.setIngameFocus();
			}
		} else {
			Menu.keyTyped(typedChar, keyCode);
		}
	}
}
