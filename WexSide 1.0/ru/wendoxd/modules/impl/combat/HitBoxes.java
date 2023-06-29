package ru.wendoxd.modules.impl.combat;

import net.minecraft.entity.Entity;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventEntityBoundingBox;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class HitBoxes extends Module {
	private Frame hitboxes_frame = new Frame("HitBoxes");
	public final static CheckBox hitboxes = new CheckBox("HitBoxes").markArrayList("Hitbox");
	private final static Slider xz = new Slider("XZ", 2, 0, 2, 0.1, () -> hitboxes.isEnabled(true));
	private final Slider y = new Slider("Y", 2, 0, 2, 0.1, () -> hitboxes.isEnabled(true));

	@Override
	protected void initSettings() {
		hitboxes.markSetting("HitBoxes");
		hitboxes_frame.register(hitboxes, xz, y);
		MenuAPI.combat.register(hitboxes_frame);
	}

	public static float getSizeHitboxes(Entity entity) {
		if (entity == mc.player || !hitboxes.isEnabled(false)) {
			return 0;
		} else {
			return xz.getFloatValue();
		}
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventEntityBoundingBox && hitboxes.isEnabled(false)) {
			for (Entity entity : mc.world.loadedEntityList) {
				if (!mc.isSingleplayer() && entity != mc.player && entity != null) {
					((EventEntityBoundingBox) event).setXZ((float) ((entity.width / 2) + xz.getDoubleValue()));
					((EventEntityBoundingBox) event).setY((float) (entity.height + y.getDoubleValue()));
				}
			}
		}
	}
}
