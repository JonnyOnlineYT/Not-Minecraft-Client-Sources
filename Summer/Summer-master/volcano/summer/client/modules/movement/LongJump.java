package volcano.summer.client.modules.movement;

import java.math.BigDecimal;
import java.math.RoundingMode;

import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventMove;
import volcano.summer.client.events.EventTick;
import volcano.summer.client.util.MathUtil;
import volcano.summer.client.util.TimerUtil;
import volcano.summer.client.value.ClampedValue;
import volcano.summer.client.value.Value;

public class LongJump extends Module {

	public boolean zoom;
	public boolean justEnabled;
	public static int stage;
	public static double moveSpeed;
	public static double lastDist;
	public static Value<Double> boost;
	public static TimerUtil timer;

	public LongJump() {
		super("LongJump", 0, Module.Category.MOVEMENT);
		boost = new ClampedValue<Double>("Boost", "boost", 2.3, 1.0, 12.0, this);
	}

	@Override
	public void onEnable() {
		this.zoom = true;
		this.timer = new TimerUtil();
		this.timer.reset();
	}

	@Override
	public void onDisable() {
		this.zoom = false;
		this.mc.timer.timerSpeed = 1.0F;
		moveSpeed = getBaseMoveSpeed();
		lastDist = 0.0D;
		stage = 4;
		if (this.mc.theWorld != null) {
			this.mc.thePlayer.motionX = 0.0D;
			this.mc.thePlayer.motionZ = 0.0D;
		}
	}

	public double getBaseMoveSpeed() {
		double baseSpeed = 0.2873D;
		if (this.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = this.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
		}
		return baseSpeed;
	}

	public double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	@Override
	public void onEvent(Event event) {
		if (mc.theWorld != null) {
			if (event instanceof EventTick) {
				double xDist = this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX;
				double zDist = this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ;
				lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
			}
		}
		if (event instanceof EventMove) {
			this.mc.timer.timerSpeed = 0.97F;
			setDisplayName("" + this.boost.getValue().floatValue());
			if (MathUtil.roundToPlace(this.mc.thePlayer.posY - (int) this.mc.thePlayer.posY, 3) == MathUtil
					.roundToPlace(0.41D, 3)) {
				this.mc.thePlayer.motionY = 0.0D;
			}
			if ((this.mc.thePlayer.moveStrafing <= 0.0F) && (this.mc.thePlayer.moveForward <= 0.0F)) {
				stage = 1;
			}
			if (MathUtil.roundToPlace(this.mc.thePlayer.posY - (int) this.mc.thePlayer.posY, 3) == MathUtil
					.roundToPlace(0.943D, 3)) {
				this.mc.thePlayer.motionY = 0.0D;
			}
			if ((stage == 1) && ((this.mc.thePlayer.moveForward != 0.0F) || (this.mc.thePlayer.moveStrafing != 0.0F))) {
				stage = 2;
				moveSpeed = this.boost.getValue().floatValue() * getBaseMoveSpeed() - 0.01D;
			} else if (stage == 2) {
				stage = 3;
				this.mc.thePlayer.motionY = 0.424D;
				((EventMove) event).y = 0.424D;
				moveSpeed *= 2.149802D;
			} else if (stage == 3) {
				stage = 4;
				double difference = 0.66D * (lastDist - getBaseMoveSpeed());
				moveSpeed = lastDist - difference;
			} else {
				if ((this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer,
						this.mc.thePlayer.boundingBox.offset(0.0D, this.mc.thePlayer.motionY, 0.0D)).size() > 0)
						|| (this.mc.thePlayer.isCollidedVertically)) {
					stage = 1;
				}
				moveSpeed = lastDist - lastDist / 159.0D;
			}
			moveSpeed = Math.max(moveSpeed, getBaseMoveSpeed());
			MovementInput movementInput = this.mc.thePlayer.movementInput;
			float forward = MovementInput.moveForward;
			MovementInput movementInput2 = this.mc.thePlayer.movementInput;
			float strafe = MovementInput.moveStrafe;
			float yaw = this.mc.thePlayer.rotationYaw;
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
		}
	}
}