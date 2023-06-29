package org.spray.heaven.util;

import org.spray.heaven.main.Wrapper;

import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class PlayerUtil {

	public static Vec3d placeBlock(BlockPos blockPos, EnumHand hand, boolean checkAction) {
		EntityPlayerSP player = Wrapper.getPlayer();
		WorldClient world = Wrapper.MC.world;
		PlayerControllerMP playerController = Wrapper.MC.playerController;

		if (player == null || world == null || playerController == null)
			return null;

		if (!world.getBlockState(blockPos).getMaterial().isReplaceable())
			return null;

		EnumFacing side = BlockUtil.getPlaceableSide(blockPos);

		if (side == null)
			return null;

		BlockPos neighbour = blockPos.offset(side);
		EnumFacing opposite = side.getOpposite();

		if (!BlockUtil.canBeClicked(neighbour)) {
			return null;
		}

		Vec3d hitVec = new Vec3d(neighbour).addVector(0.5, 0.5, 0.5)
				.add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
		Block neighbourBlock = world.getBlockState(neighbour).getBlock();

		EnumActionResult action = playerController.processRightClickBlock(player, world, neighbour, opposite, hitVec,
				hand);
		if (!checkAction || action == EnumActionResult.SUCCESS) {
			player.swingArm(hand);
			Wrapper.MC.setRightClickDelayTimer(4);
		}

		return hitVec;
	}

	public static Vec3d getVec3d(BlockPos pos, EnumFacing face) {
		double x = pos.getX() + 0.1D;
		double y = pos.getY() + 0.1D;
		double z = pos.getZ() + 0.1D;
		x += face.getFrontOffsetX() / 2.0D;
		z += face.getFrontOffsetZ() / 2.0D;
		y += face.getFrontOffsetY() / 2.0D;
		if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
			x += MathUtil.random(0.3D, -0.3D);
			z += MathUtil.random(0.3D, -0.3D);
		} else {
			y += MathUtil.random(0.49D, 0.5D);
		}
		if (face == EnumFacing.WEST || face == EnumFacing.EAST)
			z += MathUtil.random(0.3D, -0.3D);
		if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH)
			x += MathUtil.random(0.3D, -0.3D);
		return new Vec3d(x, y, z);
	}

}
