package volcano.summer.client.modules.movement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import optifine.BlockPosM;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventBlockBB;
import volcano.summer.client.events.EventPostMotionUpdate;

public class Dolphin extends Module {

	private int stage;
	private boolean canjump;
	private int delay;
	private int timer;

	public Dolphin() {
		super("Dolphin", 0, Category.MOVEMENT);
	}

	@Override
	public void onEnable() {
		this.stage = 0;
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventPostMotionUpdate) {
			BlockPos bp = new BlockPosM(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0D, this.mc.thePlayer.posZ);
			if ((!this.canjump) && (this.mc.theWorld.getBlockState(bp).getBlock() == Blocks.water)) {
				this.delay += 1;
				this.stage = 0;
				this.mc.thePlayer.motionY = 0.1D;
			} else if ((this.mc.thePlayer.onGround)
					&& (this.mc.theWorld.getBlockState(bp).getBlock() != Blocks.water)) {
				this.canjump = false;
				this.delay = 0;
			}
			if (this.delay > 2) {
				this.canjump = true;
			}
			if (this.canjump) {
				this.stage += 1;
				double moty = this.mc.thePlayer.motionY;
				switch (this.stage) {
				case 1:
					moty = 0.5D;
					break;
				case 2:
					moty = 0.483D;
					break;
				case 3:
					moty = 0.46D;
					break;
				case 4:
					moty = 0.42D;
					break;
				case 5:
					moty = 0.388D;
					break;
				case 6:
					moty = 0.348D;
					break;
				case 7:
					moty = 0.316D;
					break;
				case 8:
					moty = 0.284D;
					break;
				case 9:
					moty = 0.252D;
					break;
				case 10:
					moty = 0.22D;
					break;
				case 11:
					moty = 0.188D;
					break;
				case 12:
					moty = 0.166D;
					break;
				case 13:
					moty = 0.165D;
					break;
				case 14:
					moty = 0.16D;
					break;
				case 15:
					moty = 0.136D;
					break;
				case 16:
					moty = 0.11D;
					break;
				case 17:
					moty = 0.111D;
					break;
				case 18:
					moty = 0.1095D;
					break;
				case 19:
					moty = 0.073D;
					break;
				case 20:
					moty = 0.085D;
					break;
				case 21:
					moty = 0.06D;
					break;
				case 22:
					moty = 0.036D;
					break;
				case 23:
					moty = 0.04D;
					break;
				case 24:
					moty = 0.03D;
					break;
				case 25:
					moty = 0.004D;
					break;
				case 26:
					moty = 0.0025D;
					break;
				case 27:
					moty = 0.002D;
					break;
				case 28:
					moty = 0.0015D;
					break;
				case 29:
					moty = -0.025D;
					break;
				case 30:
					moty = -0.06D;
					break;
				case 31:
					moty = -0.09D;
					break;
				case 32:
					moty = -0.12D;
					break;
				case 33:
					moty = -0.15D;
					break;
				case 34:
					moty = -0.18D;
					break;
				case 35:
					moty = -0.21D;
					break;
				case 36:
					moty = -0.24D;
					break;
				case 37:
					moty = -0.28D;
					break;
				case 38:
					moty = -0.34D;
					break;
				case 39:
					moty = -0.4D;
					break;
				case 40:
					moty = -0.46D;
					break;
				case 41:
					moty = -0.52D;
					break;
				case 42:
					moty = -0.58D;
					break;
				case 43:
					moty = -0.65D;
					break;
				case 44:
					moty = -0.71D;
					break;
				case 45:
					this.canjump = false;
				}
				this.mc.thePlayer.motionY = moty;
			}
			if ((this.mc.thePlayer.moveForward == 0.0F) && (this.mc.thePlayer.moveStrafing == 0.0F)
					&& (!this.mc.thePlayer.isSneaking()) && (getColliding(0))) {
				int delay = 40;
				if (this.timer < delay) {
					this.timer += 1;
				} else {
					this.timer += 1;
					if (this.timer < delay + 5) {
						this.mc.thePlayer.motionX = 0.1D;
					} else if ((this.timer < delay + 20) && (this.timer > delay + 10)) {
						this.mc.thePlayer.motionZ = -0.1D;
					} else if ((this.timer < delay + 30) && (this.timer > delay + 20)) {
						this.mc.thePlayer.motionX = -0.1D;
					} else if ((this.timer < delay + 40) && (this.timer > delay + 30)) {
						this.mc.thePlayer.motionZ = 0.1D;
					}
					if (this.timer > delay + 40) {
						this.timer = delay;
					}
				}
			} else {
				this.timer = 0;
			}
		}
		if (event instanceof EventBlockBB) {
			if (((((EventBlockBB) event).getBlock() instanceof BlockLiquid)) && (!this.mc.thePlayer.isInWater())
					&& (!this.mc.thePlayer.func_180799_ab()) && (!this.mc.thePlayer.isSneaking())) {
				if (getColliding(0)) {
					((EventBlockBB) event).setBoundingBox(AxisAlignedBB.fromBounds(((EventBlockBB) event).getX() - 1.0D,
							((EventBlockBB) event).getY(), ((EventBlockBB) event).getZ() - 1.0D,
							((EventBlockBB) event).getX() + 0.9D, ((EventBlockBB) event).getY() + 0.86D,
							((EventBlockBB) event).getZ() + 0.9D));
				} else {
					((EventBlockBB) event).setBoundingBox(AxisAlignedBB.fromBounds(((EventBlockBB) event).getX() - 2.0D,
							((EventBlockBB) event).getY(), ((EventBlockBB) event).getZ() - 2.0D,
							((EventBlockBB) event).getX() + 2.0D, ((EventBlockBB) event).getY() + 1.0D,
							((EventBlockBB) event).getZ() + 2.0D));
				}
			}
		}
	}

	public static boolean getColliding(int i) {
		Minecraft mc = Minecraft.getMinecraft();
		int mx = i;
		int mz = i;
		int max = i;
		int maz = i;
		if (mc.thePlayer.motionX > 0.01D) {
			mx = 0;
		} else if (mc.thePlayer.motionX < -0.01D) {
			max = 0;
		}
		if (mc.thePlayer.motionZ > 0.01D) {
			mz = 0;
		} else if (mc.thePlayer.motionZ < -0.01D) {
			maz = 0;
		}
		int xmin = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX - mx);
		int ymin = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minY - 1.0D);
		int zmin = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ - mz);
		int xmax = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX + max);
		int ymax = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minY + 1.0D);
		int zmax = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ + maz);
		for (int x = xmin; x <= xmax; x++) {
			for (int y = ymin; y <= ymax; y++) {
				for (int z = zmin; z <= zmax; z++) {
					Block block = getBlock(new BlockPos(x, y, z));
					if (((block instanceof BlockLiquid)) && (!mc.thePlayer.isInsideOfMaterial(Material.lava))
							&& (!mc.thePlayer.isInsideOfMaterial(Material.water))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static Block getBlock(BlockPos pos) {
		return Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
	}
}