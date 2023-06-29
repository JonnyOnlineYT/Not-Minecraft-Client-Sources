package volcano.summer.client.modules.movement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.minecraft.util.Timer;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventMove;
import volcano.summer.client.events.EventPreMotionUpdate;
import volcano.summer.client.events.EventTick;
import volcano.summer.client.util.TimerUtil;
import volcano.summer.client.value.ModeValue;

public class Speed extends Module {
	public static ModeValue speedMode;
	TimerUtil timer = new TimerUtil();
	private double speed;
	public static int stage;
	private double moveSpeed;
	private double lastDist;
	private int level;
	public static boolean canStep;
	private boolean cancel;
	private boolean speedTick;
	private float speedTimer;
	private int timerDelay;
	public static boolean Speed = true;
	private int ticks;
	private double movementSpeed;
	private double lastDistance;

	public Speed() {
		super("Speed", 0, Module.Category.MOVEMENT);
		speedMode = new ModeValue("Mode", "Mode", "SlowHop",
				new String[] { "SlowHop", "Hop", "OnGround", "LowHop", "Guardian", "Gwen" }, this);
		if (speedMode.getStringValue().equalsIgnoreCase("Hop")) {
			this.speed = 6.0;
			this.level = 1;
			this.moveSpeed = 0.2873;
			this.speedTimer = 1.3f;
		}
	}

	@Override
	public void onEnable() {
		if (speedMode.getStringValue().equalsIgnoreCase("Hop")) {
			Timer.timerSpeed = 1.0f;
			this.cancel = false;
			this.level = 1;
			this.moveSpeed = ((mc.thePlayer == null) ? 0.2873 : this.getBaseMoveSpeed());
		}
		if (speedMode.getStringValue().equalsIgnoreCase("OnGround")) {
			if (mc.thePlayer != null) {
				this.moveSpeed = getBaseMoveSpeed();
			}
			this.lastDist = 0.0;
			stage = 2;
			mc.timer.timerSpeed = 1.0f;
		}
		if (speedMode.getStringValue().equalsIgnoreCase("Gwen")) {
			this.stage = 0;
			this.lastDistance = 0;
			if (mc.thePlayer != null)
				this.movementSpeed = (float) mc.thePlayer.getMovementSpeed();
		}
	}

	@Override
	public void onDisable() {
		Timer.timerSpeed = 1.0F;
		this.moveSpeed = getBaseMoveSpeed();
		this.level = 0;
	}

	public static double getBaseMoveSpeed() {
		double baseSpeed = 0.2873D;
		if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
		}
		return baseSpeed;
	}

	@Override
	public void onEvent(Event event) {
		if (speedMode.getStringValue().equalsIgnoreCase("Hop")) {
			setDisplayName("Hop");
			Hop2(event);
			if (mc.theWorld != null) {
				if (event instanceof EventTick) {
					double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
					double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
					this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
				}
			}
		}
		if (speedMode.getStringValue().equalsIgnoreCase("SlowHop")) {
			setDisplayName("SlowHop");
			slowhop(event);
			if (mc.theWorld != null) {
				if (event instanceof EventTick) {
					double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
					double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
					this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
				}
			}
		}
		if (speedMode.getStringValue().equalsIgnoreCase("OnGround")) {
			setDisplayName("OnGround");
			OnGround(event);
			if (mc.theWorld != null) {
				if (event instanceof EventTick) {
					double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
					double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
					this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
				}
			}
		}
		if (speedMode.getStringValue().equalsIgnoreCase("LowHop")) {
			setDisplayName("LowHop");
			lowhop(event);
			if (mc.theWorld != null) {
				if (event instanceof EventTick) {
					double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
					double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
					this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
				}
			}
		}
		if (speedMode.getStringValue().equalsIgnoreCase("Gwen")) {
			setDisplayName("Gwen");
			if (mc.theWorld != null) {
				if (event instanceof EventTick) {
					if (!Summer.moduleManager.getModule(Flight.class).getState()) {
						if (isMoving()) {
							if (mc.thePlayer.onGround) {
								mc.thePlayer.motionY = 0.4;
								mc.thePlayer.setSpeed(this.getBaseMoveSpeed() * 1.8);
							} else {
								mc.thePlayer.setSpeed(this.getBaseMoveSpeed() * 1.6);
							}
						} else {
							mc.thePlayer.motionX = 0;
							mc.thePlayer.motionZ = 0;
						}
					}
				}
			}
		}
		if (speedMode.getStringValue().equalsIgnoreCase("Guardian")) {
			setDisplayName("Guardian");
			guardian(event);
		}
	}

	private void gwen(Event event) {
		if (mc.thePlayer == null || mc.thePlayer.inLiquid() || mc.thePlayer.onLiquid()) {
			return;
		}
		if (event instanceof EventMove) {
			EventMove move = (EventMove) event;
			if (!mc.thePlayer.isMoving()) {
				movementSpeed = mc.thePlayer.getMovementSpeed();
				return;
			}
			if (mc.thePlayer.moveForward == 0.0F && mc.thePlayer.moveStrafing == 0.0F) {
				this.movementSpeed = (float) mc.thePlayer.getMovementSpeed() * 1.05f;
			}
			if (stage == 1 && mc.thePlayer.isCollidedVertically
					&& (mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F)) {
				this.movementSpeed = (float) (2.66D * mc.thePlayer.getMovementSpeed() - 0.01D);
			}
			float diff;
			if (stage != 2 || !mc.thePlayer.isCollidedVertically
					|| mc.thePlayer.moveForward == 0.0F && mc.thePlayer.moveStrafing == 0.0F) {
				if (this.stage == 3) {
					diff = (float) (0.33f * (mc.thePlayer.getMovementSpeed() - this.lastDistance));
					this.movementSpeed = (float) (mc.thePlayer.getMovementSpeed() - diff);
				} else {
					List var14 = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
							mc.thePlayer.getEntityBoundingBox().offset(0.0D, mc.thePlayer.motionY, 0.0D));
					if ((var14.size() > 0 || mc.thePlayer.isCollidedVertically) && stage > 0) {
						stage = mc.thePlayer.moveForward == 0.0F && mc.thePlayer.moveStrafing == 0.0F ? 0 : 1;
					}
					this.movementSpeed = (this.lastDistance - this.lastDistance / 210.0F);
				}
			} else {
				move.setY(((float) (mc.thePlayer.motionY = 0.4D)));
				this.movementSpeed *= 1.55D;
			}
			setMovementSpeed(Math.max(movementSpeed, mc.thePlayer.getMovementSpeed()), move);
			stage++;
		}
	}

	private void guardian(Event event) {
		if (event instanceof EventMove) {
			if (!(mc.thePlayer.isMoving())) {
				return;
			}
			mc.timer.timerSpeed = mc.thePlayer.ticksExisted % 3 == 0 ? 4 : 1;
			if (mc.thePlayer.onGround) {
				setMovementSpeed(0.6299999713897705D, (EventMove) event);
				((EventMove) event).setY((float) (mc.thePlayer.motionY = 0.2D));
			} else {
				mc.thePlayer.setSpeed((float) Math.sqrt(
						mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ));
				((EventMove) event).setY((float) (mc.thePlayer.motionY = -2.0D));
			}
		}
	}

	public void setMovementSpeed(double movementSpeed, EventMove event) {
		event.setX(-(Math.sin(Minecraft.getMinecraft().thePlayer.getDirection())
				* Math.max(movementSpeed, Minecraft.getMinecraft().thePlayer.getMovementSpeed())));
		event.setZ(Math.cos(Minecraft.getMinecraft().thePlayer.getDirection())
				* Math.max(movementSpeed, Minecraft.getMinecraft().thePlayer.getMovementSpeed()));
	}

	private void OnGround(Event event) {
		if (event instanceof EventPreMotionUpdate) {
			final EventPreMotionUpdate em2 = (EventPreMotionUpdate) event;
			mc.timer.timerSpeed = 1.085f;
			double forward2 = mc.thePlayer.movementInput.moveForward;
			double strafe2 = mc.thePlayer.movementInput.moveStrafe;
			if ((forward2 != 0.0 || strafe2 != 0.0) && !mc.thePlayer.isJumping && !mc.thePlayer.isInWater()
					&& !mc.thePlayer.isOnLadder() && !mc.thePlayer.isCollidedHorizontally) {
				em2.setY(mc.thePlayer.posY + ((mc.thePlayer.ticksExisted % 2 != 0) ? 0.4 : 0.0));
			}
			this.moveSpeed = Math.max((mc.thePlayer.ticksExisted % 2 == 0) ? 2.1 : 1.3, getBaseMoveSpeed());
			float yaw2 = mc.thePlayer.rotationYaw;
			if (forward2 == 0.0 && strafe2 == 0.0) {
				mc.thePlayer.motionX = 0.0;
				mc.thePlayer.motionZ = 0.0;
			} else {
				if (forward2 != 0.0) {
					if (strafe2 > 0.0) {
						yaw2 += ((forward2 > 0.0) ? -45 : 45);
					} else if (strafe2 < 0.0) {
						yaw2 += ((forward2 > 0.0) ? 45 : -45);
					}
					strafe2 = 0.0;
					if (forward2 > 0.0) {
						forward2 = 0.15;
					} else if (forward2 < 0.0) {
						forward2 = -0.15;
					}
				}
				if (strafe2 > 0.0) {
					strafe2 = 0.15;
				} else if (strafe2 < 0.0) {
					strafe2 = -0.15;
				}
				mc.thePlayer.motionX = forward2 * this.moveSpeed * Math.cos(Math.toRadians(yaw2 + 90.0f))
						+ strafe2 * this.moveSpeed * Math.sin(Math.toRadians(yaw2 + 90.0f));
				mc.thePlayer.motionZ = forward2 * this.moveSpeed * Math.sin(Math.toRadians(yaw2 + 90.0f))
						- strafe2 * this.moveSpeed * Math.cos(Math.toRadians(yaw2 + 90.0f));
			}
		}
	}

	public static double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	private void slowhop(Event event) {
		if (event instanceof EventMove) {
			mc.timer.timerSpeed = 1.0888F;
			if (round(mc.thePlayer.posY - (int) mc.thePlayer.posY, 3) == round(0.138D, 3)) {
				EntityPlayerSP player = mc.thePlayer;
				player.motionY -= 1.0D;
				((EventMove) event).y -= 0.0931D;
				EntityPlayerSP player2 = mc.thePlayer;
				player2.posY -= 0.0931D;
			}
			if ((stage == 2) && ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F))) {
				EntityPlayerSP player3 = mc.thePlayer;
				double n = 0.39936D;
				((EventMove) event).y = 0.4D;
				player3.motionY = 0.39936D;
				Speed = !Speed;
				mc.timer.timerSpeed = (Speed ? 1.16F : 1.0888F);
				moveSpeed *= 1.54D;
			} else if (stage == 3) {
				double difference = (Speed ? 0.66D : 0.66D) * (lastDist - getBaseMoveSpeed());
				moveSpeed = lastDist - difference;
				mc.timer.timerSpeed = (Speed ? 1.25F : 1.0F);
			} else {
				if ((mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
						mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0)
						|| (mc.thePlayer.isCollidedVertically)) {
					stage = 1;
				}
				moveSpeed = lastDist - lastDist / 159.0D;
			}
			moveSpeed = Math.max(moveSpeed, getBaseMoveSpeed());
			float forward = mc.thePlayer.movementInput.moveForward;
			float strafe = mc.thePlayer.movementInput.moveStrafe;
			float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
			if ((forward == 0.0F) && (strafe == 0.0F)) {
				((EventMove) event).x = 0.0D;
				((EventMove) event).z = 0.0D;
			} else if (forward != 0.0F) {
				if (strafe >= 1.0F) {
					yaw += (forward > 0.0F ? -45 : 45);
					strafe = 0.0F;
				} else if (strafe <= -1.0F) {
					yaw += (forward > 0.0F ? 45 : -45);
					strafe = 0.0F;
				}
				if (forward > 0.0F) {
					forward = 1.0F;
				} else if (forward < 0.0F) {
					forward = -1.0F;
				}
			}
			double mx = Math.cos(Math.toRadians(yaw + 90.0F));
			double mz = Math.sin(Math.toRadians(yaw + 90.0F));
			((EventMove) event).x = (forward * moveSpeed * mx + strafe * moveSpeed * mz);
			((EventMove) event).z = (forward * moveSpeed * mz - strafe * moveSpeed * mx);
			mc.thePlayer.stepHeight = 0.6F;
			if ((forward == 0.0F) && (strafe == 0.0F)) {
				((EventMove) event).x = 0.0D;
				((EventMove) event).z = 0.0D;
			} else if (forward != 0.0F) {
				if (strafe >= 1.0F) {
					yaw += (forward > 0.0F ? -45 : 45);
					strafe = 0.0F;
				} else if (strafe <= -1.0F) {
					yaw += (forward > 0.0F ? 45 : -45);
					strafe = 0.0F;
				}
				if (forward > 0.0F) {
					forward = 1.0F;
				} else if (forward < 0.0F) {
					forward = -1.0F;
				}
			}
			stage += 1;
		}
	}

	private void lowhop(Event event) {
		if (event instanceof EventMove) {
			mc.timer.timerSpeed = 1.07f;
			if (round(mc.thePlayer.posY - (int) mc.thePlayer.posY, 3) == round(0.138D, 3)) {
				EntityPlayerSP player = mc.thePlayer;
				player.motionY -= 1.0D;
				((EventMove) event).y -= 0.0565D;
				EntityPlayerSP player2 = mc.thePlayer;
				player2.posY -= 0.0865D;
			}
			if ((stage == 2) && ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F))) {
				EntityPlayerSP player3 = mc.thePlayer;
				double n = 0.23677D;
				((EventMove) event).y = 0.2D;
				player3.motionY = 0.54677D;
				Speed = !Speed;
				mc.timer.timerSpeed = (Speed ? 1.07F : 1.1664F);
				moveSpeed *= 1.07D;
			} else if (stage == 3) {
				double difference = (Speed ? 0.80D : 0.80D) * (lastDist - getBaseMoveSpeed());
				moveSpeed = lastDist - difference;
				mc.timer.timerSpeed = (Speed ? 1.07F : 1.1630F);
			} else {
				if ((mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
						mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0)
						|| (mc.thePlayer.isCollidedVertically)) {
					stage = 1;
				}
				moveSpeed = lastDist - lastDist / 159.0D;
			}
			moveSpeed = Math.max(moveSpeed, getBaseMoveSpeed());
			float forward = mc.thePlayer.movementInput.moveForward;
			float strafe = mc.thePlayer.movementInput.moveStrafe;
			float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
			if ((forward == 0.0F) && (strafe == 0.0F)) {
				((EventMove) event).x = 0.0D;
				((EventMove) event).z = 0.0D;
			} else if (forward != 0.0F) {
				if (strafe >= 1.0F) {
					yaw += (forward > 0.0F ? -45 : 45);
					strafe = 0.0F;
				} else if (strafe <= -1.0F) {
					yaw += (forward > 0.0F ? 45 : -45);
					strafe = 0.0F;
				}
				if (forward > 0.0F) {
					forward = 1.0F;
				} else if (forward < 0.0F) {
					forward = -1.0F;
				}
			}
			double mx = Math.cos(Math.toRadians(yaw + 90.0F));
			double mz = Math.sin(Math.toRadians(yaw + 90.0F));
			((EventMove) event).x = (forward * moveSpeed * mx + strafe * moveSpeed * mz);
			((EventMove) event).z = (forward * moveSpeed * mz - strafe * moveSpeed * mx);
			mc.thePlayer.stepHeight = 0.6F;
			if ((forward == 0.0F) && (strafe == 0.0F)) {
				((EventMove) event).x = 0.0D;
				((EventMove) event).z = 0.0D;
			} else if (forward != 0.0F) {
				if (strafe >= 1.0F) {
					yaw += (forward > 0.0F ? -45 : 45);
					strafe = 0.0F;
				} else if (strafe <= -1.0F) {
					yaw += (forward > 0.0F ? 45 : -45);
					strafe = 0.0F;
				}
				if (forward > 0.0F) {
					forward = 1.0F;
				} else if (forward < 0.0F) {
					forward = -1.0F;
				}
			}
			stage += 1;
		}
	}

	private void Hop2(Event event) {
		if (event instanceof EventMove) {
			mc.timer.timerSpeed = 1.0888F;
			if (round(mc.thePlayer.posY - (int) mc.thePlayer.posY, 3) == round(0.138D, 3)) {
				EntityPlayerSP player = mc.thePlayer;
				player.motionY -= 1.0D;
				((EventMove) event).y -= 0.0931D;
				EntityPlayerSP player2 = mc.thePlayer;
				player2.posY -= 0.0931D;
			}
			if ((stage == 2) && ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F))) {
				EntityPlayerSP player3 = mc.thePlayer;
				double n = 0.39936D;
				((EventMove) event).y = 0.4D;
				player3.motionY = 0.39936D;
				Speed = !Speed;
				mc.timer.timerSpeed = (Speed ? 1.19F : 1.0888F);
				moveSpeed *= 1.94D;
			} else if (stage == 3) {
				double difference = (Speed ? 0.66D : 0.66D) * (lastDist - getBaseMoveSpeed());
				moveSpeed = lastDist - difference;
				mc.timer.timerSpeed = (Speed ? 1.30F : 1.0F);
			} else {
				if ((mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
						mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0)
						|| (mc.thePlayer.isCollidedVertically)) {
					stage = 1;
				}
				moveSpeed = lastDist - lastDist / 159.0D;
			}
			moveSpeed = Math.max(moveSpeed, getBaseMoveSpeed());
			float forward = mc.thePlayer.movementInput.moveForward;
			float strafe = mc.thePlayer.movementInput.moveStrafe;
			float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
			if ((forward == 0.0F) && (strafe == 0.0F)) {
				((EventMove) event).x = 0.0D;
				((EventMove) event).z = 0.0D;
			} else if (forward != 0.0F) {
				if (strafe >= 1.0F) {
					yaw += (forward > 0.0F ? -45 : 45);
					strafe = 0.0F;
				} else if (strafe <= -1.0F) {
					yaw += (forward > 0.0F ? 45 : -45);
					strafe = 0.0F;
				}
				if (forward > 0.0F) {
					forward = 1.0F;
				} else if (forward < 0.0F) {
					forward = -1.0F;
				}
			}
			double mx = Math.cos(Math.toRadians(yaw + 90.0F));
			double mz = Math.sin(Math.toRadians(yaw + 90.0F));
			((EventMove) event).x = (forward * moveSpeed * mx + strafe * moveSpeed * mz);
			((EventMove) event).z = (forward * moveSpeed * mz - strafe * moveSpeed * mx);
			mc.thePlayer.stepHeight = 0.6F;
			if ((forward == 0.0F) && (strafe == 0.0F)) {
				((EventMove) event).x = 0.0D;
				((EventMove) event).z = 0.0D;
			} else if (forward != 0.0F) {
				if (strafe >= 1.0F) {
					yaw += (forward > 0.0F ? -45 : 45);
					strafe = 0.0F;
				} else if (strafe <= -1.0F) {
					yaw += (forward > 0.0F ? 45 : -45);
					strafe = 0.0F;
				}
				if (forward > 0.0F) {
					forward = 1.0F;
				} else if (forward < 0.0F) {
					forward = -1.0F;
				}
			}
			stage += 1;
		}
	}

	public static boolean isMoving() {
		return (mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F);
	}

	public static void setSpeed(float speed) {
		mc.thePlayer.motionX = (-(Math.sin(getDirection()) * speed));
		mc.thePlayer.motionZ = (Math.cos(getDirection()) * speed);
	}

	public static float getDirection() {
		float var1 = mc.thePlayer.rotationYaw;
		if (mc.thePlayer.moveForward < 0.0F) {
			var1 += 180.0F;
		}
		float forward = 1.0F;
		if (mc.thePlayer.moveForward < 0.0F) {
			forward = -0.5F;
		} else if (mc.thePlayer.moveForward > 0.0F) {
			forward = 0.5F;
		}
		if (mc.thePlayer.moveStrafing > 0.0F) {
			var1 -= 90.0F * forward;
		}
		if (mc.thePlayer.moveStrafing < 0.0F) {
			var1 += 90.0F * forward;
		}
		var1 *= 0.017453292F;
		return var1;
	}

	// private void Hop(Event event) {
	// if (event instanceof EventMove) {
	// this.speedTick = !this.speedTick;
	// ++this.timerDelay;
	// this.timerDelay %= 5;
	// if (this.timerDelay != 0) {
	// final Timer timer = mc.timer;
	// Timer.timerSpeed = 1.0f;
	// } else {
	// if (isMoving(mc.thePlayer)) {
	// Timer.timerSpeed = 32767.0f;
	// }
	// if (isMoving(mc.thePlayer)) {
	// Timer.timerSpeed = this.speedTimer;
	// final EntityPlayerSP player = mc.thePlayer;
	// player.motionX *= 1.0199999809265137;
	// final EntityPlayerSP player2 = mc.thePlayer;
	// player2.motionZ *= 1.0199999809265137;
	// }
	// }
	// if (mc.thePlayer.onGround && isMoving(mc.thePlayer)) {
	// this.level = 2;
	// }
	// if (MathUtil.round(mc.thePlayer.posY - (int) mc.thePlayer.posY, 3) ==
	// MathUtil.round(0.138, 3)) {
	// final EntityPlayerSP thePlayer3;
	// final EntityPlayerSP thePlayer = thePlayer3 = mc.thePlayer;
	// thePlayer3.motionY -= 0.08;
	// ((EventMove) event).y -= 0.09316090325960147;
	// final EntityPlayerSP thePlayer4;
	// final EntityPlayerSP thePlayer2 = thePlayer4 = mc.thePlayer;
	// thePlayer4.posY -= 0.09316090325960147;
	// }
	// if (this.level == 1 && (mc.thePlayer.moveForward != 0.0f ||
	// mc.thePlayer.moveStrafing != 0.0f)) {
	// this.level = 2;
	// this.moveSpeed = 1.35 * this.getBaseMoveSpeed() - 0.01;
	// } else if (this.level == 2) {
	// this.level = 3;
	// mc.thePlayer.motionY = 0.399399995803833;
	// ((EventMove) event).y = 0.399399995803833;
	// this.moveSpeed *= 2.149;
	// } else if (this.level == 3) {
	// this.level = 4;
	// final double difference = 0.66 * (this.lastDist -
	// this.getBaseMoveSpeed());
	// this.moveSpeed = this.lastDist - difference;
	// } else {
	// if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
	// mc.thePlayer.boundingBox.offset(0.0, mc.thePlayer.motionY, 0.0),
	// true).size() > 0
	// || mc.thePlayer.isCollidedVertically) {
	// this.level = 1;
	// }
	// this.moveSpeed = this.lastDist - this.lastDist / 159.0;
	// }
	// this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
	// final MovementInput movementInput = mc.thePlayer.movementInput;
	// float forward = movementInput.moveForward;
	// float strafe = movementInput.moveStrafe;
	// float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
	// if (forward == 0.0f && strafe == 0.0f) {
	// ((EventMove) event).x = 0.0;
	// ((EventMove) event).z = 0.0;
	// } else if (forward != 0.0f) {
	// if (strafe >= 1.0f) {
	// yaw += ((forward > 0.0f) ? -45 : 45);
	// strafe = 0.0f;
	// } else if (strafe <= -1.0f) {
	// yaw += ((forward > 0.0f) ? 45 : -45);
	// strafe = 0.0f;
	// }
	// if (forward > 0.0f) {
	// forward = 1.0f;
	// } else if (forward < 0.0f) {
	// forward = -1.0f;
	// }
	// }
	// final double mx = Math.cos(Math.toRadians(yaw + 90.0f));
	// final double mz = Math.sin(Math.toRadians(yaw + 90.0f));
	// final double motionX = forward * this.moveSpeed * mx + strafe *
	// this.moveSpeed * mz;
	// final double motionZ = forward * this.moveSpeed * mz - strafe *
	// this.moveSpeed * mx;
	// ((EventMove) event).x = forward * this.moveSpeed * mx + strafe *
	// this.moveSpeed * mz;
	// ((EventMove) event).z = forward * this.moveSpeed * mz - strafe *
	// this.moveSpeed * mx;
	// this.canStep = true;
	// mc.thePlayer.stepHeight = 0.6f;
	// if (forward == 0.0f && strafe == 0.0f) {
	// ((EventMove) event).x = 0.0;
	// ((EventMove) event).z = 0.0;
	// } else {
	// boolean collideCheck = false;
	// if (Minecraft.getMinecraft().theWorld
	// .getCollidingBoundingBoxes(mc.thePlayer,
	// mc.thePlayer.boundingBox.expand(0.5, 0.0, 0.5))
	// .size() > 0) {
	// collideCheck = true;
	// }
	// if (forward != 0.0f) {
	// if (strafe >= 1.0f) {
	// yaw += ((forward > 0.0f) ? -45 : 45);
	// strafe = 0.0f;
	// } else if (strafe <= -1.0f) {
	// yaw += ((forward > 0.0f) ? 45 : -45);
	// strafe = 0.0f;
	// }
	// if (forward > 0.0f) {
	// forward = 1.0f;
	// } else if (forward < 0.0f) {
	// forward = -1.0f;
	// }
	// }
	// }
	// }
	// }
}
