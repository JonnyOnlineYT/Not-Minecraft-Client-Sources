package org.spray.heaven.features.module.modules.render;

import java.awt.Color;
import java.util.Arrays;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.main.Wrapper;

import net.minecraft.entity.Entity;

@ModuleInfo(name = "CustomModels", category = Category.RENDER, visible = true, key = Keyboard.KEY_NONE)
public class CustomModels extends Module {

	public final Setting mode = register(new Setting("Mode", "Amogus", Arrays.asList("Amogus", "Demon")));

	public final Setting onlySelf = register(new Setting("Only Self", false));
	public final Setting friends = register(new Setting("Friends", true));
	public final Setting friendHighlight = register(new Setting("Friends Highlight", true));

	public final Setting bodyColor = register(new Setting("Body Color", new Color(0xFFFD2D2D))
			.setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Amogus")));
	public final Setting eyeColor = register(
			new Setting("Eye Color", Color.CYAN).setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Amogus")));
	public final Setting legsColor = register(new Setting("Legs Color", new Color(0xFFFD2D2D))
			.setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Amogus")));

	@Override
	public void onUpdate() {
		setSuffix(mode.getCurrentMode());
	}

	public boolean isValid(Entity entity) {
		return (!onlySelf.isToggle() || entity == mc.player
				|| Wrapper.getFriend().contains(entity.getName()) && friends.isToggle());
	}

}
