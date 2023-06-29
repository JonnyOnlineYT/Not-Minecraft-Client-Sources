package org.spray.infinity.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spray.infinity.features.module.movement.NoSwim;
import org.spray.infinity.main.Infinity;

import net.minecraft.entity.LivingEntity;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	
	@Inject(method = "isInSwimmingPose", at = @At("HEAD"), cancellable = true)
	public void isInSwimmingPose(CallbackInfoReturnable<Boolean> ci) {
		if (Infinity.getModuleManager().get(NoSwim.class).isEnabled()) 
			ci.setReturnValue(false);
	}

}
