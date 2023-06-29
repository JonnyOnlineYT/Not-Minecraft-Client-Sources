package ru.wendoxd.modules.impl.movement;

import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.Vec3d;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.menu.EventSwapState;
import ru.wendoxd.events.impl.misc.EventPreMove;
import ru.wendoxd.events.impl.packet.EventReceivePacket;
import ru.wendoxd.events.impl.player.EventAction;
import ru.wendoxd.events.impl.player.EventMove;
import ru.wendoxd.events.impl.player.EventPostMove;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.modules.impl.combat.Aura;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.SelectBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.player.MoveUtils;

public class Speed extends Module {
	public Frame strafe_frame = new Frame("Strafe");
	public final static CheckBox strafe_CheckBox = (CheckBox) new CheckBox("Strafe").markArrayList("Strafe")
			.markSetting("Strafe").markDescription("Стрейфы под Matrix\n!могут флагаться.!");
	public final CheckBox strafeBoost = (CheckBox) new CheckBox("Boost", () -> strafe_CheckBox.isEnabled(true))
			.markSetting("Strafe");
	public final Slider strafeValue = new Slider("Value", 2, 0.1, 0.2, 0.5, () -> strafeBoost.isEnabled(true));
	public Frame speed_frame = new Frame("Speed");
	public final CheckBox speed = new CheckBox("Speed").markArrayList("Speed");
	public final SelectBox mode = new SelectBox("Mode", new String[] { "Sunrise DMG", "Matrix", "NCP" },
			() -> speed.isEnabled(true));
	public final CheckBox autojump = new CheckBox("AutoJump",
			() -> speed.isEnabled(true) && mode.get() == 1 || mode.get() == 2);
	public final CheckBox strafe = (CheckBox) new CheckBox("Strafe", () -> speed.isEnabled(true) && mode.get() == 1)
			.markDescription("Будет совершать стрейф при прыжке");
	public static boolean needSprintState;
	public double boost;
	public static int waterTicks;
	public double moveSpeed;
	public static int stage;
	public double less;
	public double stair;
	public boolean slowDownHop;

	@Override
	protected void initSettings() {
		speed_frame.register(speed.markSetting("Speed"), mode, strafe, autojump);
		MenuAPI.movement.register(speed_frame);
		strafe_frame.register(strafe_CheckBox, strafeBoost, strafeValue);
		MenuAPI.movement.register(strafe_frame);
	}

	public int ticksInAir;

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventPreMove && mode.get() == 2 && speed.isEnabled(false)) {
			if (autojump.isEnabled(false) && !mc.gameSettings.keyBindJump.isKeyDown()) {
				return;
			}
			EventPreMove move = (EventPreMove) event;
			Speed.mc.player.jumpMovementFactor = (float) ((double) Speed.mc.player.jumpMovementFactor * 1.04);
			boolean collided = Speed.mc.player.isCollidedHorizontally;
			if (collided) {
				stage = -1;
			}
			if (this.stair > 0.0) {
				this.stair -= 0.3;
			}
			this.less -= this.less > 1.0 ? 0.24 : 0.17;
			if (this.less < 0.0) {
				this.less = 0.0;
			}
			if (!Speed.mc.player.isInWater() && Speed.mc.player.onGround) {
				collided = Speed.mc.player.isCollidedHorizontally;
				if (stage >= 0 || collided) {
					stage = 0;
					float motY = 0.42f;
					Speed.mc.player.motionY = motY;
					if (this.stair == 0.0) {
						move.setY(motY);
					}
					this.less += 1.0;
					this.slowDownHop = this.less > 1.0 && !this.slowDownHop;
					if (this.less > 1.15) {
						this.less = 1.15;
					}
				}
			}
			this.moveSpeed = this.getCurrentSpeed(stage) + 0.0335;
			this.moveSpeed *= 0.85;
			if (this.stair > 0.0) {
				this.moveSpeed *= 1.0;
			}
			if (this.slowDownHop) {
				this.moveSpeed *= 0.8575;
			}
			if (Speed.mc.player.isInWater()) {
				this.moveSpeed = 0.351;
			}
			if (MoveUtils.isMoving()) {
				MoveUtils.setEventSpeed(move, (float) this.moveSpeed);
			}
			++stage;
		}
		if (event instanceof EventAction) {
			if (strafes()) {
				if (Aura.hitTick) {
					Aura.hitTick = false;
					return;
				}
				EventAction action = (EventAction) event;
				if (CPacketEntityAction.lastUpdatedSprint != needSprintState) {
					action.setSprintState(!CPacketEntityAction.lastUpdatedSprint);
				}
			}
		}
		if (event instanceof EventMove) {
			if (MoveUtils.isInLiquid()) {
				waterTicks = 10;
			} else {
				waterTicks--;
			}
			if (strafes()) {
				EventMove move = (EventMove) event;
				double forward = mc.player.movementInput.moveForward;
				double strafe = mc.player.movementInput.moveStrafe;
				float yaw = mc.player.rotationYaw;
				if (forward == 0.0 && strafe == 0.0) {
					MatrixStrafeMovement.oldSpeed = 0;
					move.motion().xCoord = 0;
					move.motion().zCoord = 0;
				} else {
					boolean ely = strafeBoost.isEnabled(false);
					if (ely) {
						int elytra = GlideFly.getSlotIDFromItem(Items.ELYTRA);
						if (elytra == -1) {
							ely = false;
						} else {
							if (System.currentTimeMillis() - GlideFly.lastStartFalling > 1000) {
								GlideFly.disabler(elytra);
							}
						}
					}
					if (forward != 0.0) {
						if (strafe > 0.0) {
							yaw += ((forward > 0.0) ? -45 : 45);
						} else if (strafe < 0.0) {
							yaw += ((forward > 0.0) ? 45 : -45);
						}
						strafe = 0.0;
						if (forward > 0.0) {
							forward = 1.0;
						} else if (forward < 0.0) {
							forward = -1.0;
						}
					}
					double speed = MatrixStrafeMovement.calculateSpeed(move, ely, strafeValue.getDoubleValue());
					move.motion().xCoord = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f))
							+ strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
					move.motion().zCoord = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f))
							- strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
				}
			} else {
				MatrixStrafeMovement.oldSpeed = 0;
			}
		}
		if (event instanceof EventPostMove) {
			MatrixStrafeMovement.postMove(((EventPostMove) event).getHorizontalMove());
		}
		if (event instanceof EventReceivePacket) {
			if (strafes()) {
				EventReceivePacket erp = (EventReceivePacket) event;
				Packet packet = erp.getPacket();
				if (packet instanceof SPacketPlayerPosLook) {
					MatrixStrafeMovement.oldSpeed = 0;
				}
			}
		}
		if (event instanceof EventAction) {
			MatrixStrafeMovement.actionEvent((EventAction) event);
		}
		if (event instanceof EventUpdate && speed.isEnabled(false)) {
			if (mode.get() == 0) {
				double radians = MoveUtils.getDirection();
				if (MoveUtils.isMoving()) {
					if (mc.player.onGround) {
						mc.player.addVelocity(-Math.sin(radians) * 9.5 / 24.5, 0, Math.cos(radians) * 9.5 / 24.5);
						MoveUtils.setStrafe(MoveUtils.getSpeed());
					} else if (mc.player.isInWater()) {
						mc.player.addVelocity(-Math.sin(radians) * 8.5 / 24.5, 0, Math.cos(radians) * 9.5 / 24.5);
						MoveUtils.setStrafe(MoveUtils.getSpeed());
					} else if (!mc.player.onGround) {
						mc.player.addVelocity(-Math.sin(radians) * 0.11 / 24.5, 0, Math.cos(radians) * 0.11 / 24.5);
						MoveUtils.setStrafe(MoveUtils.getSpeed());
					} else {
						mc.player.addVelocity(-Math.sin(radians) * 0.005 * MoveUtils.getSpeed(), 0,
								Math.cos(radians) * 0.005 * MoveUtils.getSpeed());
						MoveUtils.setStrafe(MoveUtils.getSpeed());
					}
				}
			} else if (mode.get() == 1) {
				if (mc.player.onGround && autojump.isEnabled(false)) {
					mc.player.jump();
				}
			}
		}
		if (event instanceof EventMove) {
			Vec3d move = ((EventMove) event).motion();
			boolean fromGround = mc.player.onGround;
			boolean toGround = ((EventMove) event).toGround();
			if (mc.player.fallDistance >= 3 && toGround) {
				Vec3d to = ((EventMove) event).to();
			}
		}
		if (event instanceof EventMove && speed.isEnabled(false) && mode.get() == 1) {
			EventMove move = (EventMove) event;
			if (!mc.player.onGround && move.toGround() && mc.player.fallDistance > 0.5) {
				double speed = 2;
				if (strafe.isEnabled(false)) {
					double[] newSpeed = MoveUtils
							.getSpeed((Math.hypot(mc.player.motionX, mc.player.motionZ) - 0.0001) * speed);
					move.motion().xCoord = newSpeed[0];
					move.motion().zCoord = newSpeed[1];
					mc.player.motionX = move.motion().xCoord;
					mc.player.motionZ = move.motion().zCoord;
					return;
				}
				move.motion().xCoord *= speed;
				move.motion().zCoord *= speed;
				mc.player.motionX *= speed;
				mc.player.motionZ *= speed;
				MatrixStrafeMovement.oldSpeed *= speed;
			}
		}
		if (event instanceof EventSwapState) {
			if (((EventSwapState) event).getCheckBox() == speed) {
				if (!((EventSwapState) event).getState()) {
					mc.timer.timerSpeed = 1;
				}
			}
		}
	}

	public static boolean strafes() {
		if (!strafe_CheckBox.isEnabled(false)) {
			return false;
		}
		if (mc.player.isSneaking()) {
			return false;
		}
		if (mc.player.isInLava()) {
			return false;
		}
		if (mc.player.isInWater() || waterTicks > 0) {
			return false;
		}
		if (mc.player.isInWeb) {
			return false;
		}
		if (mc.player.capabilities.isFlying) {
			return false;
		}
		return true;
	}

	public double getCurrentSpeed(int stage) {
		double speed = MoveUtils.getBaseSpeed() + 0.028 * (double) MoveUtils.getSpeedEffect()
				+ (double) MoveUtils.getSpeedEffect() / 15.0;
		double initSpeed = 0.4145 + (double) MoveUtils.getSpeedEffect() / 12.5;
		double decrease = (double) stage / 500.0 * 1.87;
		if (stage == 0) {
			speed = 0.64 + ((double) MoveUtils.getSpeedEffect() + 0.028 * (double) MoveUtils.getSpeedEffect()) * 0.134;
		} else if (stage == 1) {
			speed = initSpeed;
		} else if (stage >= 2) {
			speed = initSpeed - decrease;
		}
		return Math.max(speed,
				this.slowDownHop ? speed : MoveUtils.getBaseSpeed() + 0.028 * (double) MoveUtils.getSpeedEffect());
	}
}
