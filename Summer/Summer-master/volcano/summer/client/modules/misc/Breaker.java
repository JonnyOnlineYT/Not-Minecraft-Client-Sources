package volcano.summer.client.modules.misc;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventPreMotionUpdate;
import volcano.summer.client.value.ClampedValue;
import volcano.summer.client.value.Value;

public class Breaker extends Module {

	ArrayList<Vector3f> positions;
	private long lastMs;
	public Value<Double> delay = new ClampedValue<Double>("BDelay", "breakerDelay", 120.0, 0.0, 1000.0, this);
	public Value<Double> reach = new ClampedValue<Double>("BReach", "breakerReach", 6.0, 1.0, 100.0, this);
	public static Breaker CLASS;

	public Breaker() {
		super("Breaker", 0, Category.MISC);
		CLASS = this;
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventPreMotionUpdate) {
			standartDestroyer((EventPreMotionUpdate) event);
		}
	}

	public boolean isDelayComplete(long delay) {
		return System.currentTimeMillis() - this.lastMs > delay;
	}

	public void reset() {
		this.lastMs = System.currentTimeMillis();
	}

	public static float[] getRotationsNeededBlock(double x, double y, double z) {
		double diffX = x + 0.5D - Minecraft.getMinecraft().thePlayer.posX;
		double diffZ = z + 0.5D - Minecraft.getMinecraft().thePlayer.posZ;

		double diffY = y + 0.5D
				- (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
		float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D);
		return new float[] {
				Minecraft.getMinecraft().thePlayer.rotationYaw
						+ MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw),
				Minecraft.getMinecraft().thePlayer.rotationPitch
						+ MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch) };
	}

	private EnumFacing getFacingDirection(BlockPos pos) {
		EntityPlayerSP p = mc.thePlayer;
		EnumFacing direction = null;
		if (!mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock().isSolidFullCube()) {
			direction = EnumFacing.UP;
		} else if (!mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock().isSolidFullCube()) {
			direction = EnumFacing.DOWN;
		} else if (!mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock().isSolidFullCube()) {
			direction = EnumFacing.EAST;
		} else if (!mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock().isSolidFullCube()) {
			direction = EnumFacing.WEST;
		} else if (!mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isSolidFullCube()) {
			direction = EnumFacing.SOUTH;
		} else if (!mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isSolidFullCube()) {
			direction = EnumFacing.NORTH;
		}
		MovingObjectPosition rayResult = mc.theWorld.rayTraceBlocks(new Vec3(p.posX, p.posY + p.getEyeHeight(), p.posZ),
				new Vec3(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5));
		if (rayResult != null && rayResult.func_178782_a() == pos) {
			return rayResult.field_178784_b;
		}
		return direction;
	}

	public static boolean blockChecks(Block block) {
		return block == Blocks.bed || block == Blocks.dragon_egg || block == Blocks.cake;
	}

	private void standartDestroyer(EventPreMotionUpdate event) {
		EntityPlayerSP p = mc.thePlayer;
		for (int y = 6; y >= -6; --y) {
			for (int x = -6; x <= 6; ++x) {
				for (int z = -6; z <= 6; ++z) {
					boolean uwot = x != 0 || z != 0;
					if (p.isSneaking()) {
						uwot = !uwot;
					}
					if (uwot) {
						BlockPos pos = new BlockPos(p.posX + x, p.posY + y, p.posZ + z);
						if (getFacingDirection(pos) != null && blockChecks(mc.theWorld.getBlockState(pos).getBlock())
								&& p.getDistance(p.posX + x, p.posY + y, p.posZ + z) < this.reach.getValue()
										.floatValue()) {
							float[] rot = getRotationsNeededBlock(pos.getX(), pos.getY(), pos.getZ());
							event.yaw = rot[0];
							event.pitch = rot[1];
							if (this.isDelayComplete((long) this.delay.getValue().floatValue())) {
								this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
										C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.DOWN));
								this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
										C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.DOWN));
								this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
										C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.DOWN));
								this.mc.thePlayer.swingItem();
								/*
								 * if (this.teleport.getValue()) {
								 * this.mc.thePlayer.sendQueue
								 * .addToSendQueue(new
								 * C03PacketPlayer.C04PacketPlayerPosition(pos.
								 * getX(), pos.getY() + 1, pos.getZ(), true)); }
								 */
								this.reset();
							}
						}
					}
				}

			}
		}
	}
}