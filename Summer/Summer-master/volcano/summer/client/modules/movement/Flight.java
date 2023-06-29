package volcano.summer.client.modules.movement;

import java.security.SecureRandom;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Timer;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventUpdate;
import volcano.summer.client.util.MathUtil;
import volcano.summer.client.util.TimerUtil;
import volcano.summer.client.value.ModeValue;

public class Flight extends Module {

	public static ModeValue flightMode;
	TimerUtil timer = new TimerUtil();
	private int counter;
	private boolean hypixelboost;

	public Flight() {
		super("Flight", 0, Module.Category.MOVEMENT);
		flightMode = new ModeValue("FlyMode", "Mode", "Creative",
				new String[] { "Creative", "Hypixel", "Mineplex", "Cubecraft", "AAC 3.3.12" }, this);
	}

	@Override
	public void onEnable() {
		hypixelboost = true;
		// if (this.flightMode.getStringValue().equalsIgnoreCase("Hypixel")) {
		// Summer.moduleManager.getModule(NoSlowDown.class).setState(false);
		// }
	}

	@Override
	public void onDisable() {

		if ((mc.thePlayer != null) && (mc.theWorld != null)) {
			mc.timer.timerSpeed = 1.0F;
			mc.thePlayer.capabilities.isFlying = false;
			mc.thePlayer.capabilities.allowFlying = false;
			mc.thePlayer.speedInAir = 0.02F;
		}
		hypixelboost = false;
		// if (this.flightMode.getStringValue().equalsIgnoreCase("Hypixel")) {
		// Summer.moduleManager.getModule(NoSlowDown.class).setState(true);
		// }
	}

	@Override
	public void onEvent(Event event) {
		if (this.flightMode.getStringValue().equalsIgnoreCase("Creative")) {
			setDisplayName("Creative");
			if (event instanceof EventUpdate) {
				mc.thePlayer.capabilities.isFlying = true;
			}
		}
		if (this.flightMode.getStringValue().equalsIgnoreCase("AAC 3.3.12")) {
			setDisplayName("AAC 3.3.12");
			if (event instanceof EventUpdate) {
				if (mc.thePlayer.hurtTime > 0 && mc.thePlayer.posY <= 1.0D)
					mc.thePlayer.motionY = 8D;
			}
		}
		if (this.flightMode.getStringValue().equalsIgnoreCase("Mineplex")) {
			setDisplayName("Mineplex");
			if (event instanceof EventUpdate) {
				if ((mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f)) {
					mc.thePlayer.setSpeed(Speed.getBaseMoveSpeed() * 1.6);

					if (mc.thePlayer.isCollidedVertically) {
						mc.thePlayer.motionY = 2.3;
						if (mc.thePlayer.motionY > 2) {
							mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.1, mc.thePlayer.posZ);
						}
					}
				}
			}
		}
		if (this.flightMode.getStringValue().equalsIgnoreCase("Cubecraft")) {
			setDisplayName("Cubecraft");
			if (event instanceof EventUpdate) {
				mc.timer.timerSpeed = 1.0F;
	        	mc.thePlayer.onGround = false;
	        	mc.thePlayer.jumpMovementFactor = 0;
	        	mc.thePlayer.motionY = -0.4;
	        	mc.thePlayer.setSpeed(0.2);

	        }
		}
		if (this.flightMode.getStringValue().equalsIgnoreCase("Hypixel")) {
			setDisplayName("Hypixel");
			if (event instanceof EventUpdate) {
				mc.thePlayer.isMoving();
				mc.thePlayer.onGround = true;
				if (!hypixelboost) {
					mc.thePlayer.cameraYaw = 0.105F;
				}
				mc.timer.timerSpeed = mc.thePlayer.ticksExisted % 2 == 0 ? 1.323531f : 1.3f;
				if (mc.gameSettings.keyBindJump.getIsKeyPressed()) {
					Timer.timerSpeed = 0.3F;
					mc.thePlayer.motionX = 0;
					mc.thePlayer.motionZ = 0;
					mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY + 0.1, mc.thePlayer.posZ);
					mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY + 0.1, mc.thePlayer.posZ);
					mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY + 0.1, mc.thePlayer.posZ);
					mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY + 0.1, mc.thePlayer.posZ);
					Timer.timerSpeed = 2.0F;
				}
				if (mc.thePlayer.ticksExisted % 2 == 0) {
					mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.28E-9D, mc.thePlayer.posZ);
				}
				if (mc.thePlayer.ticksExisted % 25 == 0 && !mc.thePlayer.onGround && !mc.thePlayer.isSwingInProgress) {
					mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1.28E-10D, mc.thePlayer.posZ);
				}
				mc.thePlayer.motionY = 0.0;
			}
		}
//		if (this.flightMode.getStringValue().equalsIgnoreCase("HypixelTEST")) {
//			setDisplayName("HypixelTEST");
//			mc.thePlayer.motionY = 0;
//            if (mc.thePlayer.ticksExisted % 2 == 0) {
//                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - MathUtil.randDouble(0.0000000010000, 0.0000000099999), mc.thePlayer.posZ);
//            } else {
//                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + MathUtil.randDouble(0.0000000010000, 0.0000000099999), mc.thePlayer.posZ);
//            }
//            if (mc.thePlayer.isMoving()) {
//                mc.timer.timerSpeed = (mc.thePlayer.ticksExisted % 2 == 0 ? 0.9f : 1.3f);
//                mc.thePlayer.setSpeed(mc.thePlayer.getBaseMovementSpeed());
//            } else {
//                mc.thePlayer.setSpeed(0);
//            }
//            if (mc.gameSettings.keyBindJump.isPressed())
//                mc.thePlayer.motionY += 0.45f;
//            if (mc.gameSettings.keyBindSneak.isPressed())
//                mc.thePlayer.motionY -= 0.45f;
//		}
	}
	

	public static double getBaseMoveSpeed() {
		double baseSpeed = 0.2793;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			final int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
		}
		return baseSpeed;
	}

	public boolean isMoving() {
		return (mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F);
	}

	public void setSpeed(double d) {
		mc.thePlayer.motionX = (-(Math.sin(getDirection()) * d));
		mc.thePlayer.motionZ = (Math.cos(getDirection()) * d);
	}

	private long lastMS = getCurrentMS();
	private long prevTime;

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

	public static void setSpeed(final float speed) {
		final EntityPlayerSP player = mc.thePlayer;
		double yaw = player.rotationYaw;
		final boolean isMoving = player.moveForward != 0.0f || player.moveStrafing != 0.0f;
		final boolean isMovingForward = player.moveForward > 0.0f;
		final boolean isMovingBackward = player.moveForward < 0.0f;
		final boolean isMovingRight = player.moveStrafing > 0.0f;
		final boolean isMovingLeft = player.moveStrafing < 0.0f;
		final boolean isMovingSideways = isMovingLeft || isMovingRight;
		final boolean isMovingStraight = isMovingForward || isMovingBackward;
		if (isMoving) {
			if (isMovingForward && !isMovingSideways) {
				yaw += 0.0;
			} else if (isMovingBackward && !isMovingSideways) {
				yaw += 180.0;
			} else if (isMovingForward && isMovingLeft) {
				yaw += 45.0;
			} else if (isMovingForward) {
				yaw -= 45.0;
			} else if (!isMovingStraight && isMovingLeft) {
				yaw += 90.0;
			} else if (!isMovingStraight && isMovingRight) {
				yaw -= 90.0;
			} else if (isMovingBackward && isMovingLeft) {
				yaw += 135.0;
			} else if (isMovingBackward) {
				yaw -= 135.0;
			}
			yaw = Math.toRadians(yaw);
			player.motionX = -Math.sin(yaw) * speed;
			player.motionZ = Math.cos(yaw) * speed;
		}
	}

	public float getDirection() {
		float yaw = mc.thePlayer.rotationYawHead;
		final float forward = mc.thePlayer.moveForward;
		final float strafe = mc.thePlayer.moveStrafing;
		yaw += ((forward < 0.0f) ? 180 : 0);
		if (strafe < 0.0f) {
			yaw += ((forward == 0.0f) ? 90 : ((forward < 0.0f) ? -45 : 45));
		}
		if (strafe > 0.0f) {
			yaw -= ((forward == 0.0f) ? 90 : ((forward < 0.0f) ? -45 : 45));
		}
		return yaw * 0.017453292f;
	}
}