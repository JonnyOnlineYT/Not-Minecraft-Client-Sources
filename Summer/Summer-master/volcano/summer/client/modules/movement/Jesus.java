package volcano.summer.client.modules.movement;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventBlockBB;
import volcano.summer.client.events.EventPacketSend;
import volcano.summer.client.events.EventPreMotionUpdate;
import volcano.summer.client.events.EventUpdate;
import volcano.summer.client.util.BlockUtil;
import volcano.summer.client.value.ModeValue;

public class Jesus extends Module {

	public static ModeValue jesusMode;

	public Jesus() {
		super("Jesus", 0, Category.MOVEMENT);
		jesusMode = new ModeValue("JesusMode", "Mode", "Solid", new String[] { "Solid", "Bounce" }, this);
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	private boolean isGoodToJesusMyDudes() {
		return (mc.theWorld != null) && (this.mc.thePlayer.fallDistance < 3.0F)
				&& (!this.mc.gameSettings.keyBindJump.isPressed()) && (!BlockUtil.isInLiquid())
				&& (!this.mc.thePlayer.isSneaking());
	}

	public static boolean isInLiquid() {
		if (Minecraft.getMinecraft().thePlayer == null) {
			return false;
		}
		boolean inLiquid = false;
		int y = (int) Minecraft.getMinecraft().thePlayer.boundingBox.minY;
		for (int x = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minX); x < MathHelper
				.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxX) + 1; x++) {
			for (int z = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minZ); z < MathHelper
					.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxZ) + 1; z++) {
				Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
				if ((block != null) && (!(block instanceof BlockAir))) {
					if (!(block instanceof BlockLiquid)) {
						return false;
					}
					inLiquid = true;
				}
			}
		}
		return inLiquid;
	}

	public static boolean isOnLiquid() {
		if (Minecraft.getMinecraft().thePlayer == null) {
			return false;
		}
		boolean onLiquid = false;
		int y = (int) Minecraft.getMinecraft().thePlayer.boundingBox.offset(0.0D, -0.01D, 0.0D).minY;
		for (int x = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minX); x < MathHelper
				.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxX) + 1; x++) {
			for (int z = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minZ); z < MathHelper
					.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxZ) + 1; z++) {
				Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
				if ((block != null) && (!(block instanceof BlockAir))) {
					if (!(block instanceof BlockLiquid)) {
						return false;
					}
					onLiquid = true;
				}
			}
		}
		return onLiquid;
	}

	public boolean getColliding(int i) {
		int mx = i;
		int mz = i;
		int max = i;
		int maz = i;
		if (Minecraft.thePlayer.motionX > 0.01D) {
			mx = 0;
		} else if (Minecraft.thePlayer.motionX < -0.01D) {
			max = 0;
		}
		if (Minecraft.thePlayer.motionZ > 0.01D) {
			mz = 0;
		} else if (Minecraft.thePlayer.motionZ < -0.01D) {
			maz = 0;
		}
		int xmin = MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().minX - mx);
		int ymin = MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().minY - 1.0D);
		int zmin = MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().minZ - mz);
		int xmax = MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().minX + max);
		int ymax = MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().minY + 1.0D);
		int zmax = MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().minZ + maz);
		for (int x = xmin; x <= xmax; x++) {
			for (int y = ymin; y <= ymax; y++) {
				for (int z = zmin; z <= zmax; z++) {
					Block block = getBlock(new BlockPos(x, y, z));
					if (((block instanceof BlockLiquid)) && (!Minecraft.thePlayer.isInsideOfMaterial(Material.lava))
							&& (!Minecraft.thePlayer.isInsideOfMaterial(Material.water))) {
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

	@Override
	public void onEvent(Event event) {
		if (this.jesusMode.getStringValue().equalsIgnoreCase("Solid")) {
			setDisplayName("Solid");
			if (event instanceof EventBlockBB) {
				if (((((EventBlockBB) event).getBlock() instanceof BlockLiquid)) && (isGoodToJesusMyDudes())) {
					((EventBlockBB) event).setBoundingBox(
							new AxisAlignedBB(((EventBlockBB) event).getX(), ((EventBlockBB) event).getY(),
									((EventBlockBB) event).getZ(), ((EventBlockBB) event).getX() + 1.0D,
									((EventBlockBB) event).getY() + 1.0D, ((EventBlockBB) event).getZ() + 1.0D));
				}
			}
		}
		if (event instanceof EventUpdate) {
			if ((BlockUtil.isInLiquid()) && (!this.mc.thePlayer.isSneaking())
					&& (!Keyboard.isKeyDown(this.mc.gameSettings.keyBindJump.getKeyCode()))) {
				this.mc.thePlayer.motionY = 0.131D;
			}
		}
		if (event instanceof EventPacketSend) {
			if (((((EventPacketSend) event).getPacket() instanceof C03PacketPlayer)) && (isGoodToJesusMyDudes())
					&& (BlockUtil.isOnLiquid())) {
				C03PacketPlayer packet = (C03PacketPlayer) ((EventPacketSend) event).getPacket();

				packet.setPositionY(this.mc.thePlayer.ticksExisted % 2 == 0 ? packet.getPositionY() + 0.01D
						: packet.getPositionY() - 0.01D);
			}
		}
		if (this.jesusMode.getStringValue().equalsIgnoreCase("Bounce")) {
			setDisplayName("Bounce");
			if (event instanceof EventPreMotionUpdate) {
				if (((!this.mc.gameSettings.keyBindSneak.pressed) && (isOnLiquid()))
						|| ((this.mc.thePlayer.isInWater()) && (!this.mc.gameSettings.keyBindSneak.pressed))) {
					this.mc.thePlayer.motionY = 0.019999999552965164D;
				}
			}
			if (event instanceof EventBlockBB) {
				EventBlockBB eventBB = (EventBlockBB) event;
				if (((eventBB.getBlock() instanceof BlockLiquid)) && (this.mc.thePlayer.fallDistance < 3.0F)
						&& (!this.mc.gameSettings.keyBindJump.isPressed()) && (!isInLiquid())
						&& (!this.mc.thePlayer.isSneaking())) {
					eventBB.setBoundingBox(new AxisAlignedBB(eventBB.getX(), eventBB.getY(), eventBB.getZ(),
							eventBB.getX() + 1.0D, eventBB.getY() + 0.99D, eventBB.getZ() + 1.0D));
				}
			}
		}
	}
}
/*
 * if (this.jesusMode.getStringValue().equalsIgnoreCase("Lava")) {
 * setDisplayName("Lava"); if ((event instanceof EventPreMotionUpdate)) { int
 * posX = (int) Math.floor(Minecraft.thePlayer.posX); int posY = (int)
 * Math.floor(Minecraft.thePlayer.getEntityBoundingBox().minY); int posZ = (int)
 * Math.floor(Minecraft.thePlayer.posZ); if (getColliding(1)) {
 * Minecraft.thePlayer.isSneaking(); } if ((getColliding(0)) &&
 * (!Minecraft.thePlayer.isCollidedVertically) &&
 * (mc.theWorld.isAnyLiquid(Minecraft.thePlayer.getEntityBoundingBox())) &&
 * (Minecraft.thePlayer.isInsideOfMaterial(Material.air)) &&
 * (!Minecraft.thePlayer.isSneaking())) { Minecraft.thePlayer.motionY = (0.085D
 * * (Minecraft.thePlayer.isCollidedHorizontally ? 1 : 1)); } if
 * ((getColliding(1)) && (!Minecraft.thePlayer.isInWater())) { if
 * (Minecraft.thePlayer.isSprinting()) {
 * Minecraft.thePlayer.setSprinting(false);
 * Minecraft.thePlayer.sendQueue.addToSendQueue(new
 * C0BPacketEntityAction(Minecraft.thePlayer,
 * C0BPacketEntityAction.Action.STOP_SPRINTING)); } if
 * ((Minecraft.thePlayer.moveForward == 0.0F) &&
 * (Minecraft.thePlayer.moveStrafing == 0.0F) &&
 * (!Minecraft.thePlayer.isSneaking()) && (getColliding(0))) { int delay = 40;
 * if (this.timer < delay) { this.timer += 1; } else { this.timer += 1; if
 * (this.timer < delay + 10) { Minecraft.thePlayer.motionX = 0.005D; } else if
 * ((this.timer < delay + 20) && (this.timer > delay + 10)) {
 * Minecraft.thePlayer.motionZ = -0.005D; } else if ((this.timer < delay + 30)
 * && (this.timer > delay + 20)) { Minecraft.thePlayer.motionX = -0.005D; } else
 * if ((this.timer < delay + 40) && (this.timer > delay + 30)) {
 * Minecraft.thePlayer.motionZ = 0.005D; } if (this.timer > delay + 40) {
 * this.timer = delay; } } } else { this.timer = 0; } this.getdown =
 * (!this.getdown); } else { this.getdown = false; } } if (((event instanceof
 * EventPacketSend)) && ((((EventPacketSend) event).getPacket() instanceof
 * C03PacketPlayer)) && (getColliding(1))) { C03PacketPlayer prepacket =
 * (C03PacketPlayer) ((EventPacketSend) event).getPacket();
 * 
 * ((EventPacketSend) event).setPacket(new
 * C03PacketPlayer.C06PacketPlayerPosLook(Minecraft.thePlayer.posX,
 * Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ,
 * Minecraft.thePlayer.rotationYaw, Minecraft.thePlayer.rotationPitch, true));
 * 
 * C03PacketPlayer packet = (C03PacketPlayer) ((EventPacketSend)
 * event).getPacket(); if ((this.getdown) && ((((EventPacketSend)
 * event).getPacket() instanceof C03PacketPlayer))) { ((EventPacketSend) event)
 * .setPacket(new
 * C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX,
 * Minecraft.thePlayer.posY + 1.0E-4D, Minecraft.thePlayer.posZ, false)); } } if
 * (((event instanceof EventBlockBB)) && ((((EventBlockBB) event).getBlock()
 * instanceof BlockLiquid)) && (!Minecraft.thePlayer.isInWater()) &&
 * (!Minecraft.thePlayer.func_180799_ab()) &&
 * (!Minecraft.thePlayer.isSneaking()) && (Minecraft.thePlayer.fallDistance <=
 * 3.0F)) { if (getColliding(0)) { ((EventBlockBB)
 * event).setBoundingBox(AxisAlignedBB.fromBounds(((EventBlockBB) event).getX()
 * - 1.0D, ((EventBlockBB) event).getY(), ((EventBlockBB) event).getZ() - 1.0D,
 * ((EventBlockBB) event).getX() + 0.9D, ((EventBlockBB) event).getY() +
 * 0.99999D, ((EventBlockBB) event).getZ() + 0.9D)); } else { ((EventBlockBB)
 * event).setBoundingBox(AxisAlignedBB.fromBounds(((EventBlockBB) event).getX()
 * - 2.0D, ((EventBlockBB) event).getY(), ((EventBlockBB) event).getZ() - 2.0D,
 * ((EventBlockBB) event).getX() + 2.0D, ((EventBlockBB) event).getY() + 1.0D,
 * ((EventBlockBB) event).getZ() + 2.0D)); } } } }
 */