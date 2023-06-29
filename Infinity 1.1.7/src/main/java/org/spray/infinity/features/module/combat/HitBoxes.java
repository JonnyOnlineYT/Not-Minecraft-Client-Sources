package org.spray.infinity.features.module.combat;

import org.spray.infinity.event.RotationEvent;
import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.StringUtil;
import org.spray.infinity.utils.entity.EntityUtil;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.entity.Entity;

@ModuleInfo(category = Category.COMBAT, desc = "Edit entity hitbox", key = -2, name = "HitBoxes", visible = true)
public class HitBoxes extends Module {

	// targets
	private Setting players = new Setting(this, "Players", true);
	private Setting invisibles = new Setting(this, "Invisibles", false);
	private Setting mobs = new Setting(this, "Mobs", true);
	private Setting animals = new Setting(this, "Animals", false);

	public Setting size = new Setting(this, "Size", 0.35D, 0.0D, 5.0D);

	@Override
	public void onUpdate() {
		setSuffix(StringUtil.DF(size.getCurrentValueDouble(), 2));
	}

	@EventTarget
	public void onRotation(RotationEvent event) {
		// spoofing to entity rotation

		if (Helper.MC.targetedEntity == null && !EntityUtil.isTarget(Helper.MC.targetedEntity,
				players.isToggle(), false, invisibles.isToggle(), mobs.isToggle(), animals.isToggle()))
			return;

		event.setYaw(Helper.getPlayer().getYaw());
		event.setPitch(Helper.getPlayer().getPitch());
		event.cancel();

	}

	public boolean isTarget(Entity entity) {
		if (EntityUtil.isTarget(entity, players.isToggle(), false, invisibles.isToggle(), mobs.isToggle(),
				animals.isToggle())) {
			return true;
		}
		return false;
	}

	public float getSize(Entity entity) {
		if (!isEnabled())
			return 0;
		if (isTarget(entity))
			return (float) size.getCurrentValueDouble();
		return 0;
	}

}
