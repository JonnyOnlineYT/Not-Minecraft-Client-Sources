package volcano.summer.client.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.network.Packet;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import volcano.summer.base.Summer;

public class BlockUtil {

	public static Minecraft mc = Minecraft.getMinecraft();

	public static boolean isInLiquid() {
		boolean inLiquid = false;
		if (Summer.mc.thePlayer == null || Summer.mc.thePlayer.boundingBox == null) {
			return false;
		}
		final int y = (int) Summer.mc.thePlayer.boundingBox.minY;
		for (int x = MathHelper.floor_double(Summer.mc.thePlayer.boundingBox.minX); x < MathHelper
				.floor_double(Summer.mc.thePlayer.boundingBox.maxX) + 1; ++x) {
			for (int z = MathHelper.floor_double(Summer.mc.thePlayer.boundingBox.minZ); z < MathHelper
					.floor_double(Summer.mc.thePlayer.boundingBox.maxZ) + 1; ++z) {
				final Block block = Summer.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
				if (block != null && !(block instanceof BlockAir)) {
					if (!(block instanceof BlockLiquid)) {
						return false;
					}
					inLiquid = true;
				}
			}
		}
		return inLiquid;
	}

	public static void sendPacketDirect(final Packet packet) {
		getSendQueue().getNetworkManager().sendPacket(packet);
	}

	public static NetHandlerPlayClient getSendQueue() {
		return mc.thePlayer.sendQueue;
	}

	public static boolean isOnLiquid() {
		boolean onLiquid = false;
		final int y = (int) (Summer.mc.thePlayer.boundingBox.minY - 0.01);
		for (int x = MathHelper.floor_double(Summer.mc.thePlayer.boundingBox.minX); x < MathHelper
				.floor_double(Summer.mc.thePlayer.boundingBox.maxX) + 1; ++x) {
			for (int z = MathHelper.floor_double(Summer.mc.thePlayer.boundingBox.minZ); z < MathHelper
					.floor_double(Summer.mc.thePlayer.boundingBox.maxZ) + 1; ++z) {
				final Block block = Summer.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
				if (block != null && !(block instanceof BlockAir)) {
					if (!(block instanceof BlockLiquid)) {
						return false;
					}
					onLiquid = true;
				}
			}
		}
		return onLiquid;
	}

	public static Block getBlock(BlockPos pos) {
		Minecraft.getMinecraft();
		return Summer.mc.theWorld.getBlockState(pos).getBlock();
	}

	public static boolean isInsideBlock() {
		for (int x = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minX); x < MathHelper
				.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxX) + 1; x++) {
			for (int y = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minY); y < MathHelper
					.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxY) + 1; y++) {
				for (int z = MathHelper
						.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minZ); z < MathHelper
								.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxZ) + 1; z++) {
					Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
					AxisAlignedBB boundingBox;
					if ((block != null) && (!(block instanceof BlockAir))
							&& ((boundingBox = block.getCollisionBoundingBox(Minecraft.getMinecraft().theWorld,
									new BlockPos(x, y, z),
									Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)))) != null)) {
						if (Minecraft.getMinecraft().thePlayer.boundingBox.intersectsWith(boundingBox)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public static Block getBlock(BlockPos pos, double offset) {
		return Minecraft.getMinecraft().thePlayer.worldObj.getBlockState(pos.add(0.0D, offset, 0.0D)).getBlock();
	}

	public static Block getBlock(final double x, final double y, final double z) {
		if (Summer.mc.theWorld != null) {
			return Summer.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
		}
		return null;
	}

	public static Block getBlock(final int x, final int y, final int z) {
		return mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
	}

	public static Block getBlock2(double posX, double posY, double posZ) {
		BlockPos bpos = new BlockPos(posX, posY, posZ);
		Block block = Minecraft.getMinecraft().theWorld.getBlockState(bpos).getBlock();
		return block;
	}

	public static float[] getFacingRotations(final int x, final int y, final int z, final EnumFacing facing) {
		final Entity temp = new EntitySnowball(mc.theWorld);
		temp.posX = x + 0.5;
		temp.posY = y + 0.5;
		temp.posZ = z + 0.5;
		final Entity entity = temp;
		entity.posX += facing.getDirectionVec().getX() * 0.25;
		final Entity entity2 = temp;
		entity2.posY += facing.getDirectionVec().getY() * 0.25;
		final Entity entity3 = temp;
		entity3.posZ += facing.getDirectionVec().getZ() * 0.25;
		return RotationUtils.getAngles(temp);
	}

	public static boolean isOnLadder() {
		boolean onLadder = false;
		int y = (int) Summer.mc.thePlayer.getEntityBoundingBox().offset(0.0D, 1.0D, 0.0D).minY;
		for (int x = MathHelper.floor_double(Summer.mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper
				.floor_double(Summer.mc.thePlayer.getEntityBoundingBox().maxX) + 1; x++) {
			for (int z = MathHelper.floor_double(Summer.mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper
					.floor_double(Summer.mc.thePlayer.getEntityBoundingBox().maxZ) + 1; z++) {
				Block block = getBlock(x, y, z);
				if ((block != null) && (!(block instanceof BlockAir))) {
					if (!(block instanceof BlockLadder)) {
						return false;
					}
					onLadder = true;
				}
			}
		}
		return (onLadder) || (Summer.mc.thePlayer.isOnLadder());
	}

	public static boolean canSeeBlock(final int x, final int y, final int z) {
		return getFacing(new BlockPos(x, y, z)) != null;
	}

	public static EnumFacing getFacing(final BlockPos pos) {
		final EnumFacing[] array;
		final EnumFacing[] orderedValues = array = new EnumFacing[] { EnumFacing.UP, EnumFacing.NORTH, EnumFacing.EAST,
				EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.DOWN };
		for (final EnumFacing facing : array) {
			final Entity temp = new EntitySnowball(mc.theWorld);
			temp.posX = pos.getX() + 0.5;
			temp.posY = pos.getY() + 0.5;
			temp.posZ = pos.getZ() + 0.5;
			final Entity entity = temp;
			entity.posX += facing.getDirectionVec().getX() * 0.5;
			final Entity entity2 = temp;
			entity2.posY += facing.getDirectionVec().getY() * 0.5;
			final Entity entity3 = temp;
			entity3.posZ += facing.getDirectionVec().getZ() * 0.5;
			if (mc.thePlayer.canEntityBeSeen(temp)) {
				return facing;
			}
		}
		return null;
	}

	public static boolean canBeClicked(final BlockPos pos) {
		return getBlock(pos).canCollideCheck(getState(pos), false);
	}

	public static IBlockState getState(final BlockPos pos) {
		return Minecraft.getMinecraft().theWorld.getBlockState(pos);
	}

//	public static Block getBlock(final BlockPos pos) {
//		return getState(pos).getBlock();
//	}

	public static Material getMaterial(final BlockPos pos) {
		return getBlock(pos).getMaterial();
	}

	public static float[] aimAtBlock(BlockPos pos) {
		EnumFacing[] arrenumFacing = EnumFacing.values();
		int n = arrenumFacing.length;
		int n2 = 0;
		float yaw = 1.0f;
		float pitch = 1.0f;
		while (n2 <= n) {
			EnumFacing side = arrenumFacing[n2];
			BlockPos neighbor = pos.offset(side);
			EnumFacing side2 = side.getOpposite();
			Vec3 hitVec = new Vec3(neighbor).addVector(0.5, 0.5, 0.5)
					.add(new Vec3(side2.getDirectionVec()).scale(0.5).normalize());

			yaw = RotationUtils.getNeededRotations(hitVec)[0];
			pitch = RotationUtils.getNeededRotations(hitVec)[1];
			if (canBeClicked(neighbor)) {
				return new float[] { yaw, pitch };
			} else {
				hitVec = new Vec3(pos).addVector(0.5, 0.5, 0.5)
						.add(new Vec3(side.getDirectionVec()).scale(0.5).normalize());
				yaw = RotationUtils.getNeededRotations(hitVec)[0];
				pitch = RotationUtils.getNeededRotations(hitVec)[1];
				return new float[] { yaw, pitch };
			}
		}
		// return new float[] {yaw,pitch};
		return new float[] { 1.0f, 1.0f };
	}
}