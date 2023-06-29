package org.spray.heaven.features.module.modules.misc;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.network.play.server.SPacketTimeUpdate;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.PacketEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;

import java.util.ArrayList;
import java.util.Arrays;

@ModuleInfo(name = "WorldTime", category = Category.MISC, visible = true, key = Keyboard.KEY_NONE)
public class WorldTime extends Module {

	private Setting mode = register(
			new Setting("Mode", "Day", new ArrayList<>(Arrays.asList("Day", "Night", "Custom"))));
	private Setting time = register(new Setting("Time", 10000, 1000, 25000))
			.setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Custom"));

	@EventTarget
	public void onPacket(PacketEvent event) {
		if (event.getType().equals(EventType.RECIEVE)) {
			if (event.getPacket() instanceof SPacketTimeUpdate)
				event.cancel();
		}
	}

	@Override
	public void onUpdate() {
		switch (mode.getCurrentMode()) {
		case "Day":
			mc.world.setWorldTime(5000);
			break;
		case "Night":
			mc.world.setWorldTime(17000);
			break;
		case "Custom":
			mc.world.setWorldTime((long) time.getValue());
			break;
		}
	}
}
