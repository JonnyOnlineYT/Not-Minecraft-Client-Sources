package org.spray.infinity.mixin;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spray.infinity.event.ClipEvent;
import org.spray.infinity.event.MoveEvent;
import org.spray.infinity.event.RotationEvent;
import org.spray.infinity.features.module.combat.HitBoxes;
import org.spray.infinity.features.module.combat.Velocity;
import org.spray.infinity.features.module.movement.AntiWaterPush;
import org.spray.infinity.main.Infinity;
import org.spray.infinity.utils.Helper;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@Mixin(Entity.class)
public abstract class EntityMixin {

	private Entity entity = (Entity) ((Object) this);

	@Shadow
	public abstract void setSwimming(boolean swimming);

	@Inject(method = "move", at = @At("HEAD"), cancellable = true)
	private void onMove(MovementType type, Vec3d movement, CallbackInfo info) {
		ClipEvent clipEvent = new ClipEvent(entity);
		EventManager.call(clipEvent);
		if (clipEvent.isCancelled() && entity == Helper.getPlayer()) {
			entity.setBoundingBox(entity.getBoundingBox().offset(movement));
			entity.setPosition(entity.getX() + movement.x, entity.getY() + movement.y, entity.getZ() + movement.z);
			info.cancel();
		}

		if (entity != Helper.getPlayer())
			return;
		MoveEvent moveEvent = new MoveEvent(EventType.PRE, type, movement);
		EventManager.call(moveEvent);
	}

	@Inject(method = "move", at = @At("RETURN"))
	private void onMovePost(MovementType type, Vec3d movement, CallbackInfo info) {
		if ((Object) this != Helper.MC.player)
			return;
		MoveEvent moveEvent = new MoveEvent(EventType.POST, type, movement);
		EventManager.call(moveEvent);
	}

	@Inject(method = "isInsideWall", at = @At("HEAD"), cancellable = true)
	public void isInsideWall(CallbackInfoReturnable<Boolean> cir) {
		ClipEvent clipEvent = new ClipEvent(entity);
		EventManager.call(clipEvent);
		if (clipEvent.isCancelled()) {
			cir.setReturnValue(false);
			cir.cancel();
		}
	}

	@Inject(method = "getTargetingMargin", at = @At("HEAD"), cancellable = true)
	public void getTargetingMargin(CallbackInfoReturnable<Float> info) {
		float box = ((HitBoxes) Infinity.getModuleManager().get(HitBoxes.class))
				.getSize((Entity) (Object) this);
		if (box != 0) {
			info.setReturnValue(box);
		}
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V", opcode = Opcodes.INVOKEVIRTUAL, ordinal = 0), method = {
			"updateMovementInFluid(Lnet/minecraft/tag/Tag;D)Z" })
	private void setVelocityFromFluid(Entity entity, Vec3d velocity) {
		if (Infinity.getModuleManager().get(AntiWaterPush.class).isEnabled())
			return;

		entity.setVelocity(velocity);
	}

	@Inject(method = "pushAwayFrom", at = @At("HEAD"), cancellable = true)
	private void onPushAwayFrom(Entity entity, CallbackInfo ci) {
		Velocity velocity = ((Velocity) Infinity.getModuleManager().get(Velocity.class));

		if (velocity.isEnabled() && velocity.mode.getCurrentMode().equalsIgnoreCase("Matrix 6.1.0"))
			velocity.pushAway((Entity) (Object) this, entity, ci);
	}

	@Inject(method = "getRotationVector", at = @At("HEAD"), cancellable = true)
	public final void getRotationVector(float pitch, float yaw, CallbackInfoReturnable<Vec3d> ci) {
		RotationEvent rotationEvent = new RotationEvent(yaw, pitch);
		EventManager.call(rotationEvent);

		if (rotationEvent.isCancelled()) {
			float f = rotationEvent.getPitch() * 0.017453292F;
			float g = -rotationEvent.getYaw() * 0.017453292F;
			float h = MathHelper.cos(g);
			float i = MathHelper.sin(g);
			float j = MathHelper.cos(f);
			float k = MathHelper.sin(f);
			ci.setReturnValue(new Vec3d((double) (i * j), (double) (-k), (double) (h * j)));
		}
	}
}
