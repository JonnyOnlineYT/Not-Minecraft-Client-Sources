package org.spray.heaven.features.module.modules.player;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.MotionEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;

@ModuleInfo(name = "AutoGapple", category = Category.PLAYER, visible = true, key = Keyboard.KEY_NONE)
public class AutoGapple extends Module {

	private Setting health = register(new Setting("Health", 15, 0, 20));
	private boolean eating;

	@Override
	public void onDisable() {
		stopEating();
	}

	@EventTarget
	public void onMotionUpdate(MotionEvent event) {
		boolean canEat = mc.player.getHeldItemOffhand().getItem() instanceof ItemAppleGold && canEat();

		if (eating && !mc.player.isHandActive()) {
			eating = false;
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
			return;
		}

		if (eating)
			return;

		if (canEat && mc.player.getHealth() + mc.player.getAbsorptionAmount() < health.getValue()) {
			mc.player.setActiveHand(EnumHand.OFF_HAND);
			eating = true;
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
			mc.rightClickMouse();
		}

	}

	private boolean canEat() {
		return mc.objectMouseOver == null || !(mc.objectMouseOver.entityHit instanceof EntityVillager);
	}

	private void stopEating() {
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
	}

}
