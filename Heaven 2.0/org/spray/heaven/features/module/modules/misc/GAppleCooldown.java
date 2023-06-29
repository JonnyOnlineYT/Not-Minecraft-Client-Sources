package org.spray.heaven.features.module.modules.misc;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spray.heaven.events.GAppleEatEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemStack;

@ModuleInfo(name = "GAppleCooldown", category = Category.MISC, visible = true, key = Keyboard.KEY_NONE)
public class GAppleCooldown extends Module {

	private final Setting cooldown = register(new Setting("Cooldown", 55, 0, 100));
	private boolean isEated;

	@Override
	public void onDisable() {
		isEated = false;
	}

	@EventTarget
	public void onEat(GAppleEatEvent event) {
		isEated = true;
	}

	@Override
	public void onUpdate() {
		if (isEated) {
			mc.player.getCooldownTracker().setCooldown(Items.GOLDEN_APPLE, (int) cooldown.getValue());
			isEated = false;
		}
		if (mc.player.getCooldownTracker().hasCooldown(Items.GOLDEN_APPLE)
				&& mc.player.getActiveItemStack().getItem() == Items.GOLDEN_APPLE) {
			mc.gameSettings.keyBindUseItem.setPressed(false);
		} else if (Mouse.isButtonDown(1) && !(mc.currentScreen instanceof GuiContainer)) {
			mc.gameSettings.keyBindUseItem.setPressed(true);
		}
	}

	private boolean isGoldenApple(ItemStack itemStack) {
		return (itemStack != null && !itemStack.isEmpty() && itemStack.getItem() instanceof ItemAppleGold);
	}
}
