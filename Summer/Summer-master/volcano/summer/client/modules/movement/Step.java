package volcano.summer.client.modules.movement;

import java.util.Arrays;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MovementInput;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventEntityStep;
import volcano.summer.client.events.EventPreMotionUpdate;
import volcano.summer.client.events.EventUpdate;
import volcano.summer.client.value.ClampedValue;
import volcano.summer.client.value.Value;

public class Step extends Module {
	double groundy = 0.0D;
	private static final double[][] RAW_OFFSETS = new double[][] { { 0.42, 0.753 }, { 0.42, 0.75, 1, 1.16, 1.23, 1.2 },
			{ 0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43 } };
	private boolean stepping;
	private double[] offsets;
	private int ticks = 0;
	public Value<Boolean> reverse = new Value<Boolean>("Reverse", "reverse", true, this);
	public Value<Double> stepHeight = new ClampedValue<Double>("Reverse Height", "reverse height", 1.0, 1.0, 2.0, this);
	public static Step THIS;

	public Step() {
		super("Step", 0, Category.MOVEMENT);
		THIS = this;
	}

	public void move(float yaw, float multiplyer) {
		double moveX = -Math.sin(Math.toRadians(yaw)) * multiplyer;
		double moveZ = Math.cos(Math.toRadians(yaw)) * multiplyer;

		this.mc.thePlayer.motionX = moveX;
		this.mc.thePlayer.motionZ = moveZ;
	}

	@Override
	public void onEnable() {
		this.stepping = false;
		mc.timer.timerSpeed = 1.0F;
	}

	@Override
	public void onDisable() {
		this.stepping = false;
		mc.timer.timerSpeed = 1.0F;
	}

	@Override
	public void onEvent(Event event) {
		setDisplayName("Smooth");
		if (event instanceof EventEntityStep) {
			if (this.mc.thePlayer == null || this.mc.theWorld == null
					|| Summer.moduleManager.getModule(Speed.class).getState())
				return;
			if (this.mc.thePlayer.isInWater() || ((EventEntityStep) event).getEntity() != this.mc.thePlayer
					|| !this.mc.thePlayer.onGround)
				return;
			float stepHeight = this.stepHeight.getValue().floatValue();
			if (this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox()
					.offset(this.mc.thePlayer.motionX, 1.6, this.mc.thePlayer.motionZ)).isEmpty())
				stepHeight = 1.5f;
			if (this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox()
					.offset(this.mc.thePlayer.motionX, 1.1, this.mc.thePlayer.motionZ)).isEmpty())
				stepHeight = 1;
			if ((stepHeight == 1 && this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer,
					this.mc.thePlayer.getEntityBoundingBox().offset(this.mc.thePlayer.motionX, 0.6,
							this.mc.thePlayer.motionZ))
					.isEmpty())
					|| !this.mc.theWorld
							.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox()
									.offset(this.mc.thePlayer.motionX, stepHeight + 0.01, this.mc.thePlayer.motionZ))
							.isEmpty())
				return;
			if (stepHeight > this.stepHeight.getValue().floatValue())
				return;
			if (stepHeight == 2) {
				for (int i = 0; i < 3; i++)
					this.mc.timer.timerSpeed = new float[] { 0.11f, 0.35f, 0.11f }[i];
			} else if (stepHeight == 1.5) {
				for (int i = 0; i < 3; i++)
					this.mc.timer.timerSpeed = new float[] { 0.155f, 0.20f, 0.155f }[i];
			} else {
				for (int i = 0; i < 3; i++)
					this.mc.timer.timerSpeed = new float[] { 0.42f, 0.75f, 0.42f }[i];
			}
			ticks = 2;
			((EventEntityStep) event).setStepHeight(stepHeight);
			stepping = true;
			offsets = RAW_OFFSETS[Math.round(stepHeight * 2) - 2];
			if (stepping) {
				Arrays.stream(offsets)
						.forEach(offset -> this.mc.thePlayer.sendQueue
								.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX,
										this.mc.thePlayer.posY + offset, this.mc.thePlayer.posZ, false)));
			}
		}
		if (event instanceof EventPreMotionUpdate) {
			if (this.mc.thePlayer == null || this.mc.theWorld == null
					|| Summer.moduleManager.getModule(Speed.class).getState())
				return;
			if (this.mc.theWorld == null)
				return;
			if (ticks == 0)
				this.mc.timer.timerSpeed = 1;
			else
				ticks--;
			if (this.mc.thePlayer != null && this.mc.thePlayer.onGround && !this.mc.thePlayer.isInWater()
					&& !this.mc.thePlayer.isInWeb && !this.mc.thePlayer.isOnLadder() && this.mc.thePlayer.onGround
					&& reverse.getValue()) {
				for (double y = 0; y < this.stepHeight.getValue().floatValue() + 0.5; y += 0.01) {
					if (!this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer,
							this.mc.thePlayer.getEntityBoundingBox().offset(0, -y, 0)).isEmpty()) {
						this.mc.thePlayer.motionY = -9;
					}
				}
			}
		}

		if ((event instanceof EventUpdate && (mc.theWorld != null))) {
			final MovementInput movementInput = mc.thePlayer.movementInput;
			final double n = MovementInput.moveForward * 0.3
					* Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f));
			final MovementInput movementInput2 = mc.thePlayer.movementInput;
			final double xOffset = n
					+ MovementInput.moveStrafe * 0.3 * Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f));
			final MovementInput movementInput3 = mc.thePlayer.movementInput;
			final double n2 = MovementInput.moveForward * 0.3
					* Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f));
			final MovementInput movementInput4 = mc.thePlayer.movementInput;
			final double zOffset = n2
					- MovementInput.moveStrafe * 0.3 * Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0f));
			final boolean one = !mc.theWorld
					.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(xOffset, 0.9, zOffset))
					.isEmpty()
					&& mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer,
							mc.thePlayer.boundingBox.offset(xOffset, 1, zOffset)).isEmpty();
			// if (!this.mc.gameSettings.keyBindForward.getIsKeyPressed()) {
			// return;
			// }
			if (this.mc.gameSettings.keyBindJump.getIsKeyPressed()) {
				return;
			}
			if ((this.mc.thePlayer.isCollidedHorizontally) && (this.mc.thePlayer.onGround) && one) {
				this.mc.thePlayer.motionY = 0.42D;
				this.groundy = this.mc.thePlayer.posY;
			} else if ((this.mc.thePlayer.posY > this.groundy + 1.0D) && (this.mc.thePlayer.motionY > 0.0D)) {

				this.mc.thePlayer.posY = (this.groundy + 1.0D);
				this.mc.thePlayer.motionY = -0.0D;
				move(this.mc.thePlayer.rotationYaw, 0.3F);
			}
		}
	}
}