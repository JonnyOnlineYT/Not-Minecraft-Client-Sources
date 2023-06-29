package org.spray.infinity.features.module.movement;

import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.utils.Helper;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

@ModuleInfo(category = Category.MOVEMENT, desc = "At the end of the block, you sit down and don't fall", key = -2, name = "Eagle", visible = true)
public class Eagle extends Module {

	@Override
	public void onDisable() {
		if (Helper.MC.options.keySneak.isPressed())
			Helper.MC.options.keySneak.setPressed(false);
	}

	@Override
	public void onUpdate() {
		BlockPos eaglePos = new BlockPos(Helper.getPlayer().getX(), Helper.getPlayer().getY() - 1,
				Helper.getPlayer().getZ());
		
		if (Helper.getPlayer().getAbilities().flying)
			return;

		if (Helper.MC.world.getBlockState(eaglePos).getBlock() != Blocks.AIR) {
			Helper.MC.options.keySneak.setPressed(false);
		} else {
			Helper.MC.options.keySneak.setPressed(true);
		}
	}

}
