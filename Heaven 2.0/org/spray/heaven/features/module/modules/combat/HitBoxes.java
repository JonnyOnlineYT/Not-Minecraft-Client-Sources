package org.spray.heaven.features.module.modules.combat;

import net.minecraft.entity.Entity;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.util.EntityUtil;
import org.spray.heaven.util.MathUtil;

@ModuleInfo(name = "HitBoxes", category = Category.COMBAT, visible = true, key = Keyboard.KEY_NONE)
public class HitBoxes extends Module {

	/**
	 * RenderManager.renderDebugBoundingBox()V extends getEntityBoundingBox
	 */
	public Setting visible = register(new Setting("Visible", true));

	private Setting size = register(new Setting("Size", 0.35, 0, 3));

	private Setting players = register(new Setting("Players", true));
	private Setting friends = register(new Setting("Friends", false).setVisible(() -> players.isToggle()));
	private Setting invisibles = register(new Setting("Invisibles", false));
	private Setting mobs = register(new Setting("Mobs", true));
	private Setting animals = register(new Setting("Animals", true));

	@Override
	public void onUpdate() {
		setSuffix(String.valueOf(MathUtil.round(size.getValue(), 2)));
	}

	/*
	 * a callback with the possibility of returning in the Entity class, the
	 * getCollisionBorderSize method
	 */
	public float getCustomSize(Entity entity) {
		KillAura killAura = ((KillAura) Wrapper.getModule().get("KillAura"));
		if (isEnabled() && killAura.isEnabled() && killAura.mode.getCurrentMode().equalsIgnoreCase("Sunrise"))
			return 0.0f;

		return (float) (isEnabled() && EntityUtil.isValid(entity, players.isToggle(), friends.isToggle(),
				invisibles.isToggle(), mobs.isToggle(), animals.isToggle()) ? size.getValue() : 0.0f);
	}
}
