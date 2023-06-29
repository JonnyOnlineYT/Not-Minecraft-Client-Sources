package ru.wendoxd.modules.impl.player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class AntiInvisible extends Module {
	public static Frame frame = new Frame("AntiInvisible");
	public static CheckBox antiInvisible = new CheckBox("AntiInvisible");

	public void initSettings() {
		frame.register(antiInvisible);
		MenuAPI.player.register(frame);
	}

	public void onEvent(Event event) {
		if (event instanceof EventUpdate && antiInvisible.isEnabled(false)) {
			for (final Entity entity : AntiInvisible.mc.world.loadedEntityList) {
				if (entity.isInvisible() && entity instanceof EntityPlayer) {
					entity.setInvisible(false);
				}
			}
		}
	}
}
