package org.spray.infinity.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spray.infinity.event.MotionEvent;
import org.spray.infinity.event.PlayerInWaterEvent;
import org.spray.infinity.event.PlayerMoveEvent;
import org.spray.infinity.event.PushOutBlockEvent;
import org.spray.infinity.event.SlowDownEvent;
import org.spray.infinity.event.VelocityEvent;
import org.spray.infinity.features.module.movement.NoSwim;
import org.spray.infinity.features.module.movement.SafeWalk;
import org.spray.infinity.features.module.player.Scaffold;
import org.spray.infinity.main.Infinity;
import org.spray.infinity.utils.Helper;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.types.EventType;
import com.mojang.authlib.GameProfile;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.MovementType;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

@Mixin(value = ClientPlayerEntity.class, priority = Integer.MAX_VALUE)
@Environment(EnvType.CLIENT)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

	public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}

	@Shadow
	public Input input;

	@Shadow
	private double lastX;

	@Shadow
	private double lastBaseY;

	@Shadow
	private double lastZ;

	@Shadow
	private float lastYaw;

	@Shadow
	private float lastPitch;

	@Shadow
	private boolean lastSneaking;

	@Shadow
	private boolean lastSprinting;

	@Shadow
	private boolean lastOnGround;

	@Shadow
	private boolean autoJumpEnabled;

	@Shadow
	private int ticksSinceLastPositionPacketSent;

	@Shadow
	@Final
	protected MinecraftClient client;

	@Shadow
	@Final
	public ClientPlayNetworkHandler networkHandler;

	@Shadow
	protected abstract boolean isCamera();

	@Shadow
	public abstract boolean isSneaking();

	@Shadow
	protected void autoJump(float float_1, float float_2) {
	}

	@Inject(method = "sendMovementPackets", at = @At("HEAD"), cancellable = true)
	public void sendMovementPackets(CallbackInfo ci) {
		MotionEvent motionEvent = new MotionEvent(EventType.PRE, this.getYaw(), this.getPitch(), this.getX(),
				this.getY(), this.getZ(), this.onGround);
		EventManager.call(motionEvent);

		if (motionEvent.isCancelled()) {
			boolean bl = this.isSprinting();
			if (bl != this.lastSprinting) {
				ClientCommandC2SPacket.Mode mode = bl ? ClientCommandC2SPacket.Mode.START_SPRINTING
						: ClientCommandC2SPacket.Mode.STOP_SPRINTING;
				this.networkHandler.sendPacket(new ClientCommandC2SPacket(this, mode));
				this.lastSprinting = bl;
			}

			boolean bl2 = this.isSneaking();
			if (bl2 != this.lastSneaking) {
				ClientCommandC2SPacket.Mode mode2 = bl2 ? ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY
						: ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY;
				this.networkHandler.sendPacket(new ClientCommandC2SPacket(this, mode2));
				this.lastSneaking = bl2;
			}

			float yaw = motionEvent.getYaw();
			float pitch = motionEvent.getPitch();

			double x = motionEvent.getX();
			double y = motionEvent.getY();
			double z = motionEvent.getZ();

			boolean onGround = motionEvent.isOnGround();

			if (this.isCamera()) {
				double d = x - this.lastX;
				double e = y - this.lastBaseY;
				double f = z - this.lastZ;
				double g = (double) (yaw - this.lastYaw);
				double h = (double) (pitch - this.lastPitch);
				++this.ticksSinceLastPositionPacketSent;
				boolean bl3 = d * d + e * e + f * f > 9.0E-4D || this.ticksSinceLastPositionPacketSent >= 20;
				boolean bl4 = g != 0.0D || h != 0.0D;
				if (this.hasVehicle()) {
					Vec3d vec3d = this.getVelocity();
					this.networkHandler
							.sendPacket(new PlayerMoveC2SPacket.Full(vec3d.x, -999.0D, vec3d.z, yaw, pitch, onGround));
					bl3 = false;
				} else if (bl3 && bl4) {
					this.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(x, y, z, yaw, pitch, onGround));
				} else if (bl3) {
					this.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, onGround));
				} else if (bl4) {
					this.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, onGround));
				} else if (this.lastOnGround != onGround) {
					this.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(onGround));
				}

				if (bl3) {
					this.lastX = x;
					this.lastBaseY = y;
					this.lastZ = z;
					this.ticksSinceLastPositionPacketSent = 0;
				}

				if (bl4) {
					this.lastYaw = yaw;
					this.lastPitch = pitch;
				}

				this.lastOnGround = onGround;
				this.autoJumpEnabled = this.client.options.autoJump;
			}

			ci.cancel();
		}

	}

	@Inject(method = "sendMovementPackets", at = @At("TAIL"), cancellable = true)
	public void sendPostMovementPackets(CallbackInfo ci) {
		MotionEvent motionEvent = new MotionEvent(EventType.POST, this.getYaw(), this.getPitch(), this.getX(),
				this.getY(), this.getZ(), this.onGround);
		EventManager.call(motionEvent);
	}

	@Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z", ordinal = 0))
	private void tickMovement(CallbackInfo ci) {
		SlowDownEvent slowDownEvent = new SlowDownEvent(input);
		EventManager.call(slowDownEvent);
	}

	@Inject(at = @At("HEAD"), method = "tick")
	private void tick(CallbackInfo info) {
		Infinity.getModuleManager().onPlayerTick();
	}

	@Inject(method = "move", at = @At("HEAD"), cancellable = true)
	public void move(MovementType movementType_1, Vec3d vec3d_1, CallbackInfo info) {
		PlayerMoveEvent moveEvent = new PlayerMoveEvent(movementType_1, vec3d_1);
		EventManager.call(moveEvent);
		if (moveEvent.isCancelled()) {
			info.cancel();
		} else if (!movementType_1.equals(moveEvent.type) || !vec3d_1.equals(moveEvent.vec3d)) {
			double double_1 = this.getX();
			double double_2 = this.getZ();
			super.move(moveEvent.type, moveEvent.vec3d);
			this.autoJump((float) (this.getX() - double_1), (float) (this.getZ() - double_2));
			info.cancel();
		}
	}

	@Override
	protected boolean clipAtLedge() {
		return super.clipAtLedge() || Infinity.getModuleManager().get(SafeWalk.class).isEnabled()
				|| (Infinity.getModuleManager().get(Scaffold.class).isEnabled()
						&& ((Scaffold) Infinity.getModuleManager().get(Scaffold.class)).safeWalk
								.isToggle());
	}

	@Inject(method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true)
	private void pushOutOfBlocks(double x, double d, CallbackInfo ci) {
		PushOutBlockEvent pushEvent = new PushOutBlockEvent();
		EventManager.call(pushEvent);
		if (pushEvent.isCancelled()) {
			ci.cancel();
		}
	}

	@Inject(method = "setSprinting", at = @At("HEAD"), cancellable = true)
	public void onSetSprinting(CallbackInfo ci) {
		if (Infinity.getModuleManager().get(NoSwim.class).isEnabled()
				&& Helper.getPlayer().isTouchingWater()) {
			ci.cancel();
		}
	}

	@Override
	public boolean isTouchingWater() {
		boolean inWater = super.isTouchingWater();
		PlayerInWaterEvent inWaterEvent = new PlayerInWaterEvent(inWater);
		EventManager.call(inWaterEvent);

		return inWaterEvent.isInWater();
	}

	@Override
	public void setVelocityClient(double x, double y, double z) {
		VelocityEvent event = new VelocityEvent(x, y, z);
		EventManager.call(event);
		super.setVelocityClient(event.getX(), event.getY(), event.getZ());
	}

}
