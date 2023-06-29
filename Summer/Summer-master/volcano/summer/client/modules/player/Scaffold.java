package volcano.summer.client.modules.player;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventMotion2;
import volcano.summer.client.events.EventPostMotionUpdate;
import volcano.summer.client.events.EventPreMotionUpdate;
import volcano.summer.client.modules.movement.Speed;
import volcano.summer.client.modules.movement.Sprint;
import volcano.summer.client.util.BlockUtil;
import volcano.summer.client.util.RotationUtils;
import volcano.summer.client.value.ModeValue;
import volcano.summer.client.value.Value;

public class Scaffold extends Module {

	public Value<Boolean> tower = new Value<Boolean>("Tower", "tower", true, this);
	public Value<Boolean> swing = new Value<Boolean>("Swing", "Swing", false, this);
	public Value<Boolean> sprint = new Value<Boolean>("Sprint", "sprint", true, this);
	public static ModeValue scaffoldMode;

	private List<Block> invalid;
	private BlockData blockData;
	public static double moveSpeed;
	private int slot;
	private double offsetToUse = 0.0d;
	private boolean handleSprintMod = false;
	public Timer timer = new Timer();
	private float oldYaw, oldPitch;
	public static Scaffold CLASS;

	public Scaffold() {
		super("Scaffold", 0, Category.PLAYER);
		this.CLASS = this;
		scaffoldMode = new ModeValue("WalkMode", "Mode", "Normal", new String[] { /* "Legit", */ "Normal", "Hypixel" },
				this);

	}

	@Override
	public void onEnable() {
		this.invalid = Arrays.asList(Blocks.air, Blocks.water, Blocks.fire, Blocks.flowing_water, Blocks.lava,
				Blocks.flowing_lava, Blocks.anvil, Blocks.chest, Blocks.ender_chest, Blocks.enchanting_table,
				Blocks.web, Blocks.torch, Blocks.redstone_lamp, Blocks.cactus, Blocks.ladder, Blocks.slime_block,
				Blocks.tripwire_hook, Blocks.dispenser);
		if (!this.sprint.getValue() && Summer.moduleManager.getModule(Sprint.class).getState()) {
			this.handleSprintMod = true;
		}

	}

	@Override
	public void onDisable() {
		if (this.handleSprintMod) {
			this.handleSprintMod = false;
		}
		this.oldYaw = 0;
		this.oldPitch = 0;
	}

	@Override
	public void onEvent(final Event event) {
//		if(scaffoldMode.getStringValue().equalsIgnoreCase("Legit")) {
//			if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ))
//					.getBlock() instanceof BlockAir && mc.thePlayer.onGround) {
//				mc.gameSettings.keyBindSneak.pressed = true;
//			} else {
//				mc.gameSettings.keyBindSneak.pressed = false;
//			}
//		}
		if (!scaffoldMode.getStringValue().equalsIgnoreCase("Hypixel")) {

			if (mc.theWorld == null)
				return;

			int block = blockInHotbar();

			if (block == 0) {
				if ((grabBlock())) {
					block = blockInHotbar();
					if (block == 0)
						return;
				} else {
					return;
				}
			}
			if (scaffoldMode.getStringValue().equalsIgnoreCase("Normal")) {
				if (event instanceof EventPreMotionUpdate) {
					int tempSlot = this.getBlockSlot();
					this.blockData = null;
					this.slot = -1;
					if (tempSlot == -1) {
						return;
					}
					if (!Minecraft.getMinecraft().gameSettings.keyBindJump.pressed) {
						timer.reset();
					}
					if (tempSlot != -1) {
						if (!this.sprint.getValue())
							mc.thePlayer.setSprinting(false);
						final double x2 = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f));
						final double z2 = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f));
						Random random = new Random();// ((double)random.nextDouble() * 5.5))//0.5 +
														// ((double)random.nextDouble() * 3.5)
						final double xOffset = mc.thePlayer.movementInput.moveForward
								* (scaffoldMode.getStringValue().equalsIgnoreCase("Hypixel")
										? (mc.gameSettings.keyBindJump.pressed ? 1.0 : 0)
										: this.offsetToUse * x2);// + mc.thePlayer.movementInput.moveStrafe *
																	// this.offsetToUse * z2;
						final double zOffset = mc.thePlayer.movementInput.moveForward
								* (scaffoldMode.getStringValue().equalsIgnoreCase("Hypixel")
										? (mc.gameSettings.keyBindJump.pressed ? 1.0 : 0)
										: this.offsetToUse * z2);// - mc.thePlayer.movementInput.moveStrafe *
																	// this.offsetToUse * x2;
						final double x = mc.thePlayer.posX + (scaffoldMode.getStringValue().equalsIgnoreCase("Hypixel")
								? (mc.gameSettings.keyBindJump.pressed ? 0.1 : 0.0)
								: xOffset);
						final double y = mc.thePlayer.posY - 0.1;
						final double z = mc.thePlayer.posZ + (scaffoldMode.getStringValue().equalsIgnoreCase("Hypixel")
								? (mc.gameSettings.keyBindJump.pressed ? 0.1 : 0.0)
								: zOffset);
						final BlockPos blockBelow1 = new BlockPos(x, y, z);
						if (mc.theWorld.getBlockState(blockBelow1).getBlock() == Blocks.air) {
							this.offsetToUse = 0.0;
							this.blockData = getBlockData(blockBelow1, this.invalid);
							this.slot = tempSlot;
							if (this.blockData != null) {
								float yaw = BlockUtil.aimAtBlock(this.blockData.position)[0];// BlockUtils.aimAtBlock(this.blockData.position)[0];
								float pitch = BlockUtil.aimAtBlock(this.blockData.position)[1];// mc.gameSettings.keyBindJump.pressed
																								// ? 90.0f :
																								// (legit.getValue() ?
																								// 90.0f
																								// :
																								// BlockUtils.aimAtBlock(this.blockData.position)[1]);
								// event.location.setYaw(yaw);//this.aimAtLocation(this.blockData.position.getX(),
								// this.blockData.position.getY(), this.blockData.position.getZ(),
								// this.blockData.face)[0]);
								// event.location.setPitch(pitch);//this.aimAtLocation(this.blockData.position.getX(),
								// this.blockData.position.getY(), this.blockData.position.getZ(),
								// this.blockData.face)[1]);
								this.oldYaw = yaw;
								this.oldPitch = pitch;
							}
						} else {
						}
					}
				}
				if (event instanceof EventPostMotionUpdate && this.blockData != null) {
					if (!this.sprint.getValue())
						mc.thePlayer.setSprinting(false);
					boolean dohax;
					boolean bl = dohax = mc.thePlayer.inventory.currentItem != this.slot;
					final int oldSlot = mc.thePlayer.inventory.currentItem;
					if (dohax) {
						mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.slot));
					}
					if (mc.playerController.func_178890_a(mc.thePlayer, mc.theWorld,
							mc.thePlayer.inventoryContainer.getSlot(36 + this.slot).getStack(), this.blockData.position,
							this.blockData.face, new Vec3(this.blockData.position.getX(),
									this.blockData.position.getY(), this.blockData.position.getZ()))) {
						if (this.tower.getValue()) {
							if (Minecraft.getMinecraft().gameSettings.keyBindJump.pressed) {
								Minecraft.getMinecraft().thePlayer.jump();
								mc.thePlayer.setSpeed(0.0f);
							}
							if (timer.hasReached(1500L) && Minecraft.getMinecraft().gameSettings.keyBindJump.pressed) {
								Minecraft.getMinecraft().thePlayer.motionY = -0.28D;
								timer.reset();
							}
							if (!Minecraft.getMinecraft().gameSettings.keyBindJump.pressed) {
								timer.reset();
							}
						}
						if (this.swing.getValue())
							mc.thePlayer.swingItem();
						else
							mc.getNetHandler().getNetworkManager().sendPacket(new C0APacketAnimation());
					}
					if (dohax) {
						// p.inventory.currentItem = oldSlot;
						mc.thePlayer.sendQueue
								.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
					}
				}
			} else {
				int tempSlot = this.getBlockSlot();
				this.blockData = null;
				this.slot = -1;
				if (tempSlot == -1) {
					return;
				}
				final double x = mc.thePlayer.posX;
				final double y = mc.thePlayer.posY - 0.1;
				final double z = mc.thePlayer.posZ;
				final BlockPos blockBelow1 = new BlockPos(x, y, z);
				final BlockPos belowPlayer = new BlockPos(mc.thePlayer).offsetDown();
				if (event instanceof EventPreMotionUpdate) {
					if (!Minecraft.getMinecraft().gameSettings.keyBindJump.pressed) {
						timer.reset();
					}
				} else {
					if (getMaterial(belowPlayer).isReplaceable()) {// mc.theWorld.getBlockState(belowPlayer).getBlock()
																	// ==
																	// Blocks.air ) {
						this.blockData = getBlockData(belowPlayer, this.invalid);
						this.slot = tempSlot;
						if (this.blockData != null) {
							if (!this.sprint.getValue())
								mc.thePlayer.setSprinting(false);
							boolean dohax;
							boolean bl = dohax = mc.thePlayer.inventory.currentItem != this.slot;
							final int oldSlot = mc.thePlayer.inventory.currentItem;
							if (dohax) {
								// p.inventory.currentItem = this.slot;
								mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.slot));
							}
							this.placeBlockHypixel(belowPlayer);
							if (dohax) {
								// p.inventory.currentItem = oldSlot;
								mc.thePlayer.sendQueue.addToSendQueue(
										new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
							}
						}
					}
				}

			}
		}
		if (mc.theWorld != null && mc.thePlayer != null) {
			int n = 0;
			try {
				for (int k = 0; k < 9; ++k) {
					ItemStack stackInSlot3 = mc.thePlayer.inventory.getStackInSlot(k);
					if (stackInSlot3 != null && stackInSlot3.getItem() instanceof ItemBlock) {
						n += stackInSlot3.stackSize;
					}
				}
			} catch (Exception ex2) {
				ex2.printStackTrace();
			}
			setDisplayName("" + n);
		}
	}

	public boolean placeBlockHypixel(final BlockPos pos) {
		final Vec3 eyesPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
				mc.thePlayer.posZ);
		EnumFacing[] values;
		for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
			final EnumFacing side = values[i];
			final BlockPos neighbor = pos.offset(side);
			final EnumFacing side2 = side.getOpposite();
			if (eyesPos.squareDistanceTo(new Vec3(pos).addVector(0.5, 0.5, 0.5)) < eyesPos
					.squareDistanceTo(new Vec3(neighbor).addVector(0.5, 0.5, 0.5))) {
				if (this.canBeClicked(neighbor)) {
					final Vec3 hitVec = new Vec3(neighbor).addVector(0.5, 0.5, 0.5)
							.add(new Vec3(side2.getDirectionVec()).scale(0.5));
					if (eyesPos.squareDistanceTo(hitVec) <= 18.0625) {
						RotationUtils.faceVectorPacketInstant(hitVec);
						mc.playerController.processRightClickBlock(mc.thePlayer, mc.theWorld,
								mc.thePlayer.inventoryContainer.getSlot(36 + this.slot).getStack(), neighbor, side2,
								hitVec);
						if (this.tower.getValue()) {
							if (Minecraft.getMinecraft().gameSettings.keyBindJump.pressed) {
								Minecraft.getMinecraft().thePlayer.jump();
								mc.thePlayer.setSpeed(0.0f);
							}
							if (timer.hasReached(1500L) && Minecraft.getMinecraft().gameSettings.keyBindJump.pressed) {
								Minecraft.getMinecraft().thePlayer.motionY = -0.28D;
								timer.reset();
							}
							if (!Minecraft.getMinecraft().gameSettings.keyBindJump.pressed) {
								timer.reset();
							}
						}
						if (this.swing.getValue())
							mc.thePlayer.swingItem();
						else
							mc.getNetHandler().getNetworkManager().sendPacket(new C0APacketAnimation());
						mc.rightClickDelayTimer = 4;
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean canBeClicked(final BlockPos pos) {
		return getBlock(pos).canCollideCheck(getState(pos), false);
	}

	public IBlockState getState(final BlockPos pos) {
		return mc.theWorld.getBlockState(pos);
	}

	public Block getBlock(final BlockPos pos) {
		return getState(pos).getBlock();
	}

	public Material getMaterial(final BlockPos pos) {
		return getBlock(pos).getMaterial();
	}

	private void grabBlocks() {
		for (int i = 9; i < 36; ++i) {
			final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
			if (stack != null && stack.getItem() instanceof ItemBlock && stack.stackSize >= 1
					&& Block.getBlockFromItem(stack.getItem()).getDefaultState().isFullBlock()) {
				final PlayerControllerMP playerController = mc.playerController;
				final int windowId = mc.thePlayer.openContainer.windowId;
				final int slotId = i;
				final int p_78753_3_ = 1;
				final int p_78753_4_ = 2;
				playerController.windowClick(windowId, slotId, p_78753_3_, p_78753_4_, mc.thePlayer);
				break;
			}
		}
	}

	public float[] aimAtBlock(BlockPos pos) {
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
					.add(new Vec3(side2.getDirectionVec()).multi(0.5).normalize());

			yaw = RotationUtils.getNeededRotations(hitVec)[0];
			pitch = RotationUtils.getNeededRotations(hitVec)[1];
			if (canBeClicked(neighbor)) {
				return new float[] { yaw, pitch };
			} else {
				hitVec = new Vec3(pos).addVector(0.5, 0.5, 0.5)
						.add(new Vec3(side.getDirectionVec()).multi(0.5).normalize());
				yaw = RotationUtils.getNeededRotations(hitVec)[0];
				pitch = RotationUtils.getNeededRotations(hitVec)[1];
				return new float[] { yaw, pitch };
			}
		}
		return new float[] { 1.0f, 1.0f };
	}

	public class Timer {

		private long lastMS;
		private long prevTime;

		public Timer() {
			lastMS = getCurrentMS();
		}

		private long getCurrentMS() {
			return System.nanoTime() / 1000000L;
		}

		public boolean hasReached(long milliseconds) {
			return getCurrentMS() - lastMS >= milliseconds;
		}

		public void reset() {
			lastMS = getCurrentMS();
			this.prevTime = this.getCurrentMS();
		}
	}

	private int getBlockSlot() {
		int i = 36;
		while (i < 45) {
			ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
			if (itemStack != null && itemStack.getItem() instanceof ItemBlock && itemStack.stackSize >= 1
					&& Block.getBlockFromItem(itemStack.getItem()).getDefaultState().isFullBlock()) {
				return i - 36;
			}
			++i;
		}
		return -1;
	}

	private int getTotalBlocks() {
		int totalCount = 0;
		int i = 9;
		while (i < 45) {
			ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
			if (itemStack != null && itemStack.getItem() instanceof ItemBlock && itemStack.stackSize >= 1) {
				totalCount += itemStack.stackSize;
			}
			++i;
		}
		return totalCount;
	}

	private BlockData getBlockData(final BlockPos pos, final List list) {
		if (!this.invalid.contains(this.mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock())) {
			return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
		}
		if (!this.invalid.contains(this.mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
			return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (!this.invalid.contains(this.mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())) {
			return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
		}
		if (!this.invalid.contains(this.mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())) {
			return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
		}
		if (!this.invalid.contains(this.mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())) {
			return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
		}
		BlockPos add = pos.add(-1, 0, 0);
		if (!this.invalid.contains(this.mc.theWorld.getBlockState(add.add(-1, 0, 0)).getBlock())) {
			return new BlockData(add.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (!this.invalid.contains(this.mc.theWorld.getBlockState(add.add(1, 0, 0)).getBlock())) {
			return new BlockData(add.add(1, 0, 0), EnumFacing.WEST);
		}
		if (!this.invalid.contains(this.mc.theWorld.getBlockState(add.add(0, 0, -1)).getBlock())) {
			return new BlockData(add.add(0, 0, -1), EnumFacing.SOUTH);
		}
		if (!this.invalid.contains(this.mc.theWorld.getBlockState(add.add(0, 0, 1)).getBlock())) {
			return new BlockData(add.add(0, 0, 1), EnumFacing.NORTH);
		}
		BlockPos add1 = pos.add(1, 0, 0);
		if (!this.invalid.contains(this.mc.theWorld.getBlockState(add1.add(-1, 0, 0)).getBlock())) {
			return new BlockData(add1.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (!this.invalid.contains(this.mc.theWorld.getBlockState(add1.add(1, 0, 0)).getBlock())) {
			return new BlockData(add1.add(1, 0, 0), EnumFacing.WEST);
		}
		if (!this.invalid.contains(this.mc.theWorld.getBlockState(add1.add(0, 0, -1)).getBlock())) {
			return new BlockData(add1.add(0, 0, -1), EnumFacing.SOUTH);
		}
		if (!this.invalid.contains(this.mc.theWorld.getBlockState(add1.add(0, 0, 1)).getBlock())) {
			return new BlockData(add1.add(0, 0, 1), EnumFacing.NORTH);
		}
		BlockPos add2 = pos.add(0, 0, -1);
		if (!this.invalid.contains(this.mc.theWorld.getBlockState(add2.add(-1, 0, 0)).getBlock())) {
			return new BlockData(add2.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (!this.invalid.contains(this.mc.theWorld.getBlockState(add2.add(1, 0, 0)).getBlock())) {
			return new BlockData(add2.add(1, 0, 0), EnumFacing.WEST);
		}
		if (!this.invalid.contains(this.mc.theWorld.getBlockState(add2.add(0, 0, -1)).getBlock())) {
			return new BlockData(add2.add(0, 0, -1), EnumFacing.SOUTH);
		}
		if (!this.invalid.contains(this.mc.theWorld.getBlockState(add2.add(0, 0, 1)).getBlock())) {
			return new BlockData(add2.add(0, 0, 1), EnumFacing.NORTH);
		}
		BlockPos add3 = pos.add(0, 0, 1);
		if (!this.invalid.contains(this.mc.theWorld.getBlockState(add3.add(-1, 0, 0)).getBlock())) {
			return new BlockData(add3.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (!this.invalid.contains(this.mc.theWorld.getBlockState(add3.add(1, 0, 0)).getBlock())) {
			return new BlockData(add3.add(1, 0, 0), EnumFacing.WEST);
		}
		if (!this.invalid.contains(this.mc.theWorld.getBlockState(add3.add(0, 0, -1)).getBlock())) {
			return new BlockData(add3.add(0, 0, -1), EnumFacing.SOUTH);
		}
		if (!this.invalid.contains(this.mc.theWorld.getBlockState(add3.add(0, 0, 1)).getBlock())) {
			return new BlockData(add3.add(0, 0, 1), EnumFacing.NORTH);
		}
		return null;
	}

	private class BlockData {
		public BlockPos position;
		public EnumFacing face;

		public BlockData(final BlockPos position, final EnumFacing face) {
			this.position = position;
			this.face = face;
		}
	}

	private boolean grabBlock() {
		for (int i = 0; i < 36; i++) {
			ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
			if (stack != null && stack.getItem() instanceof ItemBlock) {
				for (int x = 36; x < 45; x++) {
					try {
						Item stackkk = mc.thePlayer.inventoryContainer.getSlot(x).getStack().getItem();
					} catch (NullPointerException ex) {
						swap(i, x - 36);
						return true;
					}
				}
				swap(i, 1);
				return true;
			}
		}
		return false;
	}

	private void swap(final int slot, final int hotbarNum) {
		mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, mc.thePlayer);
	}

	private int blockInHotbar() {
		for (int i = 36; i < 45; i++) {
			ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
			if (stack != null && stack.getItem() instanceof ItemBlock) {
				return i;
			}
		}
		return 0;
	}

}