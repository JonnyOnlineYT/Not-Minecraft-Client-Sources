package ru.wendoxd.utils.visual.animation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.GuiScreen;
import ru.wendoxd.WexSide;
import ru.wendoxd.events.EventManager;
import ru.wendoxd.events.impl.misc.EventAnimation;
import ru.wendoxd.ui.menu.Menu;

public class AnimationThread extends Thread {
	@Override
	public void run() {
		while (true) {
			try {
				Menu.animation();
				GuiScreen.animation.update(Minecraft.getMinecraft().currentScreen != null);
				GuiPlayerTabOverlay.tick();
				WexSide.getModules().stream().filter(module -> Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world != null).forEach(module -> module.update_animation());
				EventManager.call(new EventAnimation());
				sleep(20);
			} catch (Exception ignored) {
			}
		}
	}
}
