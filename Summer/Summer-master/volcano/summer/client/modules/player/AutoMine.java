package volcano.summer.client.modules.player;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventPreMotionUpdate;

public class AutoMine extends Module {

	public AutoMine() {
		super("AutoMine", 0, Category.PLAYER);
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
		mc.gameSettings.keyBindAttack.pressed = false;
	}

	public static void setSlot(BlockPos blockPos) {
		float bestSpeed = 1.0F;
		int bestSlot = -1;
		Block block = mc.theWorld.getBlockState(blockPos).getBlock();
		for (int i = 0; i < 9; i++) {
			ItemStack item = Minecraft.thePlayer.inventory.getStackInSlot(i);
			if (item != null) {
				float speed = item.getStrVsBlock(block);
				if (speed > bestSpeed) {
					bestSpeed = speed;
					bestSlot = i;
				}
			}
		}
		if (bestSlot != -1) {
			Minecraft.thePlayer.inventory.currentItem = bestSlot;
		}
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventPreMotionUpdate) {
			if ((mc.objectMouseOver == null) || (mc.objectMouseOver.func_178782_a() == null)) {
				return;
			}
			if (mc.theWorld.getBlockState(mc.objectMouseOver.func_178782_a()).getBlock()
					.getMaterial() != Material.air) {
				mc.gameSettings.keyBindAttack.pressed = true;
				setSlot(mc.objectMouseOver.func_178782_a());
			}
		}
	}
}