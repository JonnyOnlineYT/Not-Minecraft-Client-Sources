package ru.wendoxd.modules.impl.movement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import ru.wendoxd.events.Event;
import ru.wendoxd.modules.Module;

public class Scaffold extends Module {
	float overrideYaw, overridePitch;

	@Override
	protected void initSettings() {
	}

	@Override
	public void onEvent(Event event) {
	}

	public float[] getRotationVector(final Vec3d vec) {
		final Vec3d eyesPos = mc.player.getPositionEyes(1);
		final double diffX = vec.xCoord - eyesPos.xCoord;
		final double diffY = vec.yCoord - eyesPos.yCoord;
		final double diffZ = vec.zCoord - eyesPos.zCoord;
		final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
		float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));
		pitch = MathHelper.clamp(pitch * 1.001f, -90.0f, 90.0f);
		return new float[] { yaw, pitch };
	}

	public BlockData getBlockData2(BlockPos pos) {
		BlockData blockData = null;
		int i = 0;
		while (blockData == null) {
			if (i >= 2) {
				break;
			}
			if (!this.isBlockPosAir(pos.add(0, 0, 1))) {
				blockData = new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
				break;
			}
			if (!this.isBlockPosAir(pos.add(0, 0, -1))) {
				blockData = new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
				break;
			}
			if (!this.isBlockPosAir(pos.add(1, 0, 0))) {
				blockData = new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
				break;
			}
			if (!this.isBlockPosAir(pos.add(-1, 0, 0))) {
				blockData = new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
				break;
			}
			if (Scaffold.mc.gameSettings.keyBindJump.isKeyDown() && !this.isBlockPosAir(pos.add(0, -1, 0))) {
				blockData = new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
				break;
			}
			if (!this.isBlockPosAir(pos.add(1, 0, 1))) {
				blockData = new BlockData(pos.add(1, 0, 1), EnumFacing.NORTH);
				break;
			}
			if (!this.isBlockPosAir(pos.add(-1, 0, -1))) {
				blockData = new BlockData(pos.add(-1, 0, -1), EnumFacing.SOUTH);
				break;
			}
			if (!this.isBlockPosAir(pos.add(1, 0, 1))) {
				blockData = new BlockData(pos.add(1, 0, 1), EnumFacing.WEST);
				break;
			}
			if (!this.isBlockPosAir(pos.add(-1, 0, -1))) {
				blockData = new BlockData(pos.add(-1, 0, -1), EnumFacing.EAST);
				break;
			}
			if (!this.isBlockPosAir(pos.add(-1, 0, 1))) {
				blockData = new BlockData(pos.add(-1, 0, 1), EnumFacing.NORTH);
				break;
			}
			if (!this.isBlockPosAir(pos.add(1, 0, -1))) {
				blockData = new BlockData(pos.add(1, 0, -1), EnumFacing.SOUTH);
				break;
			}
			if (!this.isBlockPosAir(pos.add(1, 0, -1))) {
				blockData = new BlockData(pos.add(1, 0, -1), EnumFacing.WEST);
				break;
			}
			if (!this.isBlockPosAir(pos.add(-1, 0, 1))) {
				blockData = new BlockData(pos.add(-1, 0, 1), EnumFacing.EAST);
				break;
			}
			pos = pos.down();
			++i;
		}
		return blockData;
	}

	private Vec3d getVectorToPlace(final BlockData data) {
		final BlockPos pos = data.pos;
		final EnumFacing face = data.face;
		double x = pos.getX() + 0.5;
		double y = pos.getY() + 0.5;
		double z = pos.getZ() + 0.5;
		if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
			x += 0.3;
			z += 0.3;
		} else {
			y += 0.5;
		}
		if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
			z += 0.15;
		}
		if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
			x += 0.15;
		}
		return new Vec3d(x, y, z);
	}

	private Vec3d getVectorToRotate(BlockData data) {
		final BlockPos pos = data.pos;
		final EnumFacing face = data.face;
		double x = pos.getX() + 0.5;
		double y = pos.getY() + 0.5;
		double z = pos.getZ() + 0.5;
		if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
			x += 0.4;
			z += 0.4;
		} else {
			y += 0.4;
		}
		return new Vec3d(x, y, z);
	}

	public boolean isBlockPosAir(final BlockPos blockPos) {
		return this.getBlockByPos(blockPos) == Blocks.AIR || this.getBlockByPos(blockPos) instanceof BlockLiquid;
	}

	public Block getBlockByPos(final BlockPos blockPos) {
		return Scaffold.mc.world.getBlockState(blockPos).getBlock();
	}

	private static class BlockData {
		public BlockPos pos;
		public EnumFacing face;

		private BlockData(final BlockPos pos, final EnumFacing face) {
			this.pos = pos;
			this.face = face;
		}
	}
}
