package org.spray.heaven.features.module.modules.movement;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;

@ModuleInfo(name = "InventoryMove", category = Category.MOVEMENT, visible = true, key = Keyboard.KEY_NONE)
public class InventoryMove extends Module {

	@Override
	public void onUpdate() {
		KeyBinding[] keys = { mc.gameSettings.keyBindRight, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindBack,
				mc.gameSettings.keyBindForward, mc.gameSettings.keyBindJump, mc.gameSettings.keyBindSprint };
		if (mc.currentScreen instanceof GuiChat || mc.currentScreen instanceof GuiEditSign)
			return;

		for (KeyBinding keyBinding : keys)
			keyBinding.setPressed(Keyboard.isKeyDown(keyBinding.getKeyCode()));
	}
}
