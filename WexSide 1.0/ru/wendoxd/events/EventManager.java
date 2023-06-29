package ru.wendoxd.events;

import net.minecraft.client.Minecraft;
import ru.wendoxd.WexSide;
import ru.wendoxd.modules.Module;

import java.util.ConcurrentModificationException;

public class EventManager {
	public static boolean call(Event event) {
		try {
			for (Module module : WexSide.getModules()) {
				if (Minecraft.getMinecraft().player != null) {
					module.onEvent(event);
				}
			}
			if (WexSide.commandManager != null) {
				WexSide.commandManager.getCommands().forEach(command -> command.onEvent(event));
			}
		} catch (ConcurrentModificationException ignore) {
		}
		if (!(event instanceof IEventCancelable)) {
			return false;
		} else {
			return ((IEventCancelable) event).isCanceled();
		}
	}
}
