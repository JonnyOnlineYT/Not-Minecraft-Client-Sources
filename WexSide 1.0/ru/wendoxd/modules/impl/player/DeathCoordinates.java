package ru.wendoxd.modules.impl.player;

import net.minecraft.client.gui.GuiGameOver;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.misc.ChatUtils;

public class DeathCoordinates extends Module {

	private Frame deathcoordinates_frame = new Frame("DeathCoordinates");
	private final CheckBox deathcoords = new CheckBox("DeathCoordinates").markArrayList("DeathCoords");
	public int lastX, lastY, lastZ;

	@Override
	protected void initSettings() {
		deathcoords.markSetting("DeathCoordinates");
		deathcoordinates_frame.register(deathcoords);
		MenuAPI.player.register(deathcoordinates_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventUpdate && deathcoords.isEnabled(false)) {
			if (mc.currentScreen instanceof GuiGameOver) {
				int x = mc.player.getPosition().getX();
				int y = mc.player.getPosition().getY();
				int z = mc.player.getPosition().getZ();
				if (lastX != x || lastY != y || lastZ != z) {
					ChatUtils.addChatMessage("Death Coordinates: " + "X: " + x + " Y: " + y + " Z: " + z);
					lastX = x;
					lastY = y;
					lastZ = z;
				}
			}
		}
	}
}
