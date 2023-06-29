package splash.client.modules.movement;

import me.hippo.systems.lwjeb.annotation.Collect;
import me.hippo.systems.lwjeb.event.Stage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.ModeValue;
import splash.client.events.network.EventPacketReceive;
import splash.client.events.player.*;
import splash.utilities.math.MathUtils;
import splash.utilities.player.MovementUtils;
import splash.utilities.time.Stopwatch;

public class Phase extends Module {
	private ModeValue<Mode> mode = new ModeValue<>("Mode", Mode.ARIS, this);
	private enum Mode {
		ARIS, FALCON, VANILLA
	}
	private double distance = 2.5;
	private Stopwatch timer;
	private int moveUnder;
	
	public Phase() {
		super("Phase", "Lets you go through blocks.", ModuleCategory.MOVEMENT);
		timer = new Stopwatch();
	}
	public static String getDirection() {
		return Minecraft.getMinecraft().thePlayer.getHorizontalFacing().getName();
	}

	@Collect
	public void onTick(EventTick eventTick) {
		if(mode.getValue() == Mode.VANILLA) {
			if (mc.thePlayer != null && moveUnder == 1) {
				mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 2.0, mc.thePlayer.posZ, false));
				mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Double.NEGATIVE_INFINITY, mc.thePlayer.posY, Double.NEGATIVE_INFINITY, true));
				moveUnder = 0;
			}
			if (mc.thePlayer != null && moveUnder == 1488) {
				double mx = -Math.sin(Math.toRadians(mc.thePlayer.rotationYaw));
				double mz = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw));
				double x = mc.thePlayer.movementInput.moveForward * mx + mc.thePlayer.movementInput.moveStrafe * mz;
				double z = mc.thePlayer.movementInput.moveForward * mz - mc.thePlayer.movementInput.moveStrafe * mx;
				mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z, false));
				mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Double.NEGATIVE_INFINITY, mc.thePlayer.posY, Double.NEGATIVE_INFINITY, true));
				moveUnder = 0;
			}
		}
	}

	public boolean isInsideBlock() {
		for (int x = MathHelper.floor_double(
				mc.thePlayer.boundingBox.minX); x < MathHelper.floor_double(mc.thePlayer.boundingBox.maxX) + 1; ++x) {
			for (int y = MathHelper.floor_double(
					mc.thePlayer.boundingBox.minY + 1.0D); y < MathHelper.floor_double(mc.thePlayer.boundingBox.maxY)
					+ 2; ++y) {
				for (int z = MathHelper.floor_double(
						mc.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(mc.thePlayer.boundingBox.maxZ)
						+ 1; ++z) {
					Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
					if (block != null && !(block instanceof BlockAir)) {
						AxisAlignedBB boundingBox = block.getCollisionBoundingBox(mc.theWorld, new BlockPos(x, y, z),
								mc.theWorld.getBlockState(new BlockPos(x, y, z)));
						if (block instanceof BlockHopper) {
							boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
						}

						if (boundingBox != null && mc.thePlayer.boundingBox.intersectsWith(boundingBox))
							return true;
					}
				}
			}
		}
		return false;
	}

	@Collect
	public void onBB(EventBoundingBox event) {
		if (mode.getValue().equals(Mode.VANILLA)) {
			if (mc.thePlayer.isCollidedHorizontally && !isInsideBlock()) {
				double mx = -Math.sin(Math.toRadians(mc.thePlayer.rotationYaw));
				double mz = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw));
				double x = mc.thePlayer.movementInput.moveForward * mx + mc.thePlayer.movementInput.moveStrafe * mz;
				double z = mc.thePlayer.movementInput.moveForward * mz - mc.thePlayer.movementInput.moveStrafe * mx;
				event.setBoundingBox(null);
				mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
				moveUnder = 69;
			}
			if (isInsideBlock()) event.setBoundingBox(null);
		}


		if ((mode.getValue().equals(Mode.ARIS) && mc.thePlayer.isSneaking() )
				&& (isInsideBlock() && mc.gameSettings.keyBindJump.pressed || !isInsideBlock() && event.getBoundingBox() != null && event.getBoundingBox().maxY > mc.thePlayer.boundingBox.minY)) {
			if (mc.thePlayer.ticksExisted % 4 == 0 && moveUnder == 1) {
				sendPosition(0,(-mc.thePlayer.posY) + .0000625, 0, false, mc.thePlayer.isMoving());
			} else {
				moveUnder = 2;
			}
			if (mc.thePlayer.isCollidedVertically) {
				mc.thePlayer.motionY = 0;
			}
			event.setBoundingBox(null);
		} else {
			mc.thePlayer.setSpeed(mc.thePlayer.isMoving() ? .15 : 0);
		}

		if (mode.getValue().equals(Mode.FALCON)) {
			if (isInsideBlock() && !Minecraft.getMinecraft().gameSettings.keyBindSprint.isKeyDown()) {
				if (event.getBlockPos().getY() > mc.thePlayer.getEntityBoundingBox().minY - 0.4) {
					if (event.getBlockPos().getY() < mc.thePlayer.getEntityBoundingBox().maxY + 1.0) {
						event.setBoundingBox(null);
					}
				}
			}
			event.setBoundingBox(null);
		}
	}

	@Collect
	public void onCollision(EventBlockPush e) {
		if (mode.getValue().equals(Mode.FALCON) && isInsideBlock() || mode.getValue().equals(Mode.ARIS) && mc.thePlayer.isSneaking() || mode.getValue().equals(Mode.VANILLA)) {
			e.setCancelled(true);
			if (mc.thePlayer.isCollidedVertically) {
				mc.thePlayer.motionY = 0;
			}
		}
	}

	@Collect
	public void onUpdate(EventPlayerUpdate eventPlayerUpdate) {
		if (mode.getValue().equals(Mode.VANILLA) && mc.gameSettings.keyBindSneak.isPressed() && !isInsideBlock()) {
			mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 2, mc.thePlayer.posZ, true));
			moveUnder = 2;
		}
	}
	
	@Collect
	public void onRenderInside(EventRenderInsideBlock e) {
		e.setCancelled(true);
	}
	
	@Collect
	public void onMove(EventMove event) {
		if(mode.getValue().equals(Mode.VANILLA)) {
			if (isInsideBlock()) {
				if (mc.gameSettings.keyBindJump.isKeyDown()) {
					event.setY(mc.thePlayer.motionY = 1.2);
				} else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
					event.setY(mc.thePlayer.motionY = -1.2);
				} else {
					event.setY(mc.thePlayer.motionY = 0.0);
				}
				event.setMoveSpeed(0.3);
			}
		}

		if (isInsideBlock() && !mode.getValue().equals(Mode.ARIS)) {
			mc.thePlayer.motionY = 0;
			if (mc.gameSettings.keyBindSneak.isKeyDown()) {
				mc.thePlayer.motionY = 0.16;
				event.setY(mc.thePlayer.motionY + .4);
			} else {
				if (mc.thePlayer.movementInput.sneak) {
					mc.thePlayer.motionY = -0.16;
					event.setY(mc.thePlayer.motionY - .1);
				} else {
					mc.thePlayer.motionY = 0;
					event.setY(0.0);
				}
			}
			event.setMoveSpeed(0.24);
		}
	} 
	
	@Collect
	public void onPacketReceive(EventPacketReceive event) {
		if (event.getReceivedPacket() instanceof S02PacketChat) {
			S02PacketChat packet = (S02PacketChat) event.getReceivedPacket();
			if (packet.getChatComponent().getUnformattedText().contains("You cannot go past the border.")) {
				event.setCancelled(true);
			}
		}

		if(mode.getValue().equals(Mode.VANILLA)) {
			if (event.getReceivedPacket() instanceof S08PacketPlayerPosLook) {
				S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getReceivedPacket();
				packet.setPitch(mc.thePlayer.rotationPitch);
				packet.setYaw(mc.thePlayer.rotationYaw);

				if (moveUnder == 2) {
					moveUnder = 1;
				}
			}
		}

		if (mode.getValue().equals(Mode.VANILLA) && event.getReceivedPacket() instanceof S08PacketPlayerPosLook && moveUnder == 2) {
			moveUnder = 1;
		}

		if (mode.getValue().equals(Mode.VANILLA) && event.getReceivedPacket() instanceof S08PacketPlayerPosLook && moveUnder == 69) {
			moveUnder = 1488;
		}
		if (event.getReceivedPacket() instanceof S08PacketPlayerPosLook) {
			S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getReceivedPacket();
			packet.setPitch(mc.thePlayer.rotationPitch);
			packet.setYaw(mc.thePlayer.rotationYaw);

			if (moveUnder == 2) {
				moveUnder = 1;
			}
		}
	}

	@Collect
	public void onTick(EventPlayerUpdate event) {
		if (mode.getValue().equals(Mode.VANILLA)) {
			mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 2.0, mc.thePlayer.posZ, true));
			moveUnder = 2;
		}


		if (event.getStage().equals(Stage.PRE)) { 
			if (mode.getValue().equals(Mode.FALCON)) {
				if (mc.thePlayer.movementInput.sneak) {
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
							mc.thePlayer.posY, mc.thePlayer.posZ, true));
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
							Double.NEGATIVE_INFINITY, 0, Double.NEGATIVE_INFINITY, true));
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
							mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround));
					moveUnder = 2;
				}
				if (moveUnder == 1) {
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
							mc.thePlayer.posY, mc.thePlayer.posZ, true));
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
							Double.NEGATIVE_INFINITY, 0, Double.NEGATIVE_INFINITY, true));
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
							mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround));
					moveUnder = 0;
				}
			}
		} else {
			if (mode.getValue().equals(Mode.ARIS)) {
				if (event.getStage().equals(Stage.PRE)) {
					event.setYaw(event.getYaw() + MathUtils.getRandomInRange(-.5F, .5F));
					if (mc.thePlayer.isSneaking()) {
						double offset = 0.27;
						if (!mc.thePlayer.isOnLadder() && moveUnder == 2 && mc.thePlayer.ticksExisted % 4 == 0) {
							mc.thePlayer.setSpeed(mc.thePlayer.isCollidedHorizontally ? offset : .05); 
							Number playerYaw = mc.thePlayer.getDirection();
							sendPosition(-Math.sin(playerYaw.doubleValue()) * offset,0, Math.cos(playerYaw.doubleValue()) * offset, false, mc.thePlayer.isMoving());
							mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX - Math.sin(playerYaw.doubleValue()) * offset, mc.thePlayer.posY, mc.thePlayer.posZ + Math.cos(playerYaw.doubleValue()) * offset);
						}

					}
				}
			} 
		}
	}
}