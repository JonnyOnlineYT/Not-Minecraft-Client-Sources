package org.spray.infinity.features.module.hidden;

import org.lwjgl.glfw.GLFW;
import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.main.Infinity;

import net.minecraft.client.MinecraftClient;

@ModuleInfo(category = Category.HIDDEN, desc = "Infinity features menu", key = GLFW.GLFW_KEY_GRAVE_ACCENT, name = "Menu", visible = false)
public class Menu extends Module {

	@Override
	public void onEnable() {
		MinecraftClient.getInstance().openScreen(Infinity.MENU);
		setState(false);
	}
}
