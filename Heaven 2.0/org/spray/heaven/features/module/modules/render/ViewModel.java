package org.spray.heaven.features.module.modules.render;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;

import net.minecraft.item.ItemStack;

@ModuleInfo(name = "ViewModel", category = Category.RENDER, visible = true, key = Keyboard.KEY_NONE)
public class ViewModel extends Module {

	public Setting scale = register(new Setting("Scale", 0.55, 0.1, 2.0));

	public Setting xRotation = register(new Setting("Rotation X", 0, -2, 2));
	public Setting yRotation = register(new Setting("Rotation Y", 0.15, -2, 2));
	public Setting zRotation = register(new Setting("Rotation Z", 0, -2, 2));

	public Setting oscale = register(new Setting("Offhand Scale", 0.55, 0.1, 2.0));

	public Setting oxRotation = register(new Setting("Offhand Rotation X", 0, -2, 2));
	public Setting oyRotation = register(new Setting("Offhand Rotation Y", 0.29, -2, 2));
	public Setting ozRotation = register(new Setting("Offhand Rotation Z", 0, -2, 2));

	// All the work takes place in the RenderItem.java

	public boolean offhandValid(ItemStack stack) {
		return mc.player.getHeldItemOffhand() == stack;
	}
}
