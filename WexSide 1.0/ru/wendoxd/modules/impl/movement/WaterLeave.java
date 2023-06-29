package ru.wendoxd.modules.impl.movement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.player.MoveUtils;

public class WaterLeave extends Module {

	private Frame waterleave_frame = new Frame("WaterLeave");
	private final CheckBox waterleave = new CheckBox("WaterLeave").markArrayList("WaterLeave");
	private final Slider motiony = new Slider("Motion Y", 1, 0, 20, 0.25, () -> waterleave.isEnabled(true));

	@Override
	protected void initSettings() {
		waterleave.markSetting("WaterLeave");
		waterleave_frame.register(waterleave, motiony);
		MenuAPI.movement.register(waterleave_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventUpdate && waterleave.isEnabled(false)) {
			if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 0.1, mc.player.posZ))
					.getBlock() instanceof BlockLiquid
					|| mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 0.1, mc.player.posZ))
							.getBlock() instanceof BlockLiquid) {
				if (collideBlock(Block::isLiquid) && mc.player.isInsideOfMaterial(Material.AIR)) {
					mc.player.motionY = 0.08;
				}
				if (!MoveUtils.isInLiquid() && mc.player.fallDistance > 0 && mc.player.motionY < 0.08) {
					mc.player.motionY += motiony.getFloatValue();
					mc.player.onGround = true;
				}
			}
		}
	}

	public static boolean collideBlock(ICollide collide) {
		for (int x = MathHelper.floor(mc.player.getEntityBoundingBox().minX); x <= MathHelper
				.floor(mc.player.getEntityBoundingBox().maxX) + 1; x++) {
			for (int z = MathHelper.floor(mc.player.getEntityBoundingBox().minZ); z <= MathHelper
					.floor(mc.player.getEntityBoundingBox().maxZ) + 1; z++) {
				Block block = mc.world.getBlockState(new BlockPos(x, mc.player.posY, z)).getBlock();
				if (collide.collideBlock(block))
					continue;
				return false;
			}
		}
		return true;
	}

	public static interface ICollide {
		public boolean collideBlock(Block block);
	}
}
