package org.spray.heaven.features.module.modules.movement;

import org.lwjgl.input.Keyboard;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.util.BlockUtil;

@ModuleInfo(name = "Eagle", category = Category.MOVEMENT, visible = true, key = Keyboard.KEY_NONE)
public class Eagle extends Module {

	@Override
	public void onDisable() {
		mc.gameSettings.keyBindSneak.setPressed(false);
	}

	@Override
	public void onUpdate() {
		BlockPos bp = new BlockPos(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY - 1.0D, Wrapper.getPlayer().posZ);
		if (mc.player.fallDistance <= 4.0F)
			if (BlockUtil.getBlock(bp) != Blocks.AIR)
				mc.gameSettings.keyBindSneak.setPressed(false);
			else if (!mc.player.capabilities.isFlying && mc.player.onGround)
				mc.gameSettings.keyBindSneak.setPressed(true);
	}

}
