package org.spray.heaven.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

import org.spray.heaven.main.Wrapper;

public class BlockUtil {

	public static Block getBlock(BlockPos pos) {
		return getState(pos).getBlock();
	}

	public static IBlockState getState(BlockPos blockPos) {
		return Wrapper.getWorld().getBlockState(blockPos);
	}

	public static Vec3d floorVec3d(Vec3d addVector) {
		return new Vec3d(Math.floor(addVector.xCoord), Math.floor(addVector.yCoord), Math.floor(addVector.zCoord));
	}

	public static boolean canBeClicked(BlockPos pos) {
		return getBlock(pos).canCollideCheck(getState(pos), false);
	}

	public static EnumFacing getPlaceableSide(BlockPos pos) {

		for (EnumFacing side : EnumFacing.values()) {

			BlockPos neighbour = pos.offset(side);

			if (Wrapper.getWorld().isAirBlock(neighbour)) {
				continue;
			}

			IBlockState blockState = getState(neighbour);
			if (!blockState.getMaterial().isReplaceable()) {
				return side;
			}
		}

		return null;
	}

	public static EnumFacing getPlaceableSideExlude(BlockPos pos, ArrayList<EnumFacing> excluding) {

		for (EnumFacing side : EnumFacing.values()) {

			if (!excluding.contains(side)) {

				BlockPos neighbour = pos.offset(side);

				if (Wrapper.getWorld().isAirBlock(neighbour)) {
					continue;
				}

				IBlockState blockState = Wrapper.getWorld().getBlockState(neighbour);
				if (!blockState.getMaterial().isReplaceable()) {
					return side;
				}
			}
		}

		return null;
	}

	public static boolean collideBlock(AxisAlignedBB axisAlignedBB, float boxYSize, ICollide collide) {
		for (int x = MathHelper.floor(Wrapper.getPlayer().getEntityBoundingBox().minX); x < MathHelper
				.floor(Wrapper.getPlayer().getEntityBoundingBox().maxX) + 1; ++x) {
			for (int z = MathHelper.floor(Wrapper.getPlayer().getEntityBoundingBox().minZ); z < MathHelper
					.floor(Wrapper.getPlayer().getEntityBoundingBox().maxZ) + 1; ++z) {
				Block block = BlockUtil.getBlock(new BlockPos(x, axisAlignedBB.minY + boxYSize, z));
				if (!collide.collideBlock(block)) {
					return false;
				}
			}
		}
		return true;
	}

	public interface ICollide {
		boolean collideBlock(Block block);
	}

}
