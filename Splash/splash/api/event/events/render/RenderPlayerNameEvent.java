package splash.api.event.events.render;

import me.hippo.systems.lwjeb.event.Cancelable;
import net.minecraft.entity.Entity;

public class RenderPlayerNameEvent extends Cancelable {
	public Entity p;

	public RenderPlayerNameEvent(final Entity p2) {
		this.p = p2;
	}
}
