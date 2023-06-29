package volcano.summer.base.manager.event;

import volcano.summer.base.Summer;
import volcano.summer.base.manager.module.Module;

public class EventManager {

	public static void call(Event event) {
		for (Module m : Summer.moduleManager.mods) {
			if (!m.state)
				continue;
			m.onEvent(event);
		}

	}
}
