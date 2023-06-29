package org.spray.infinity.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spray.infinity.features.module.world.CameraClip;
import org.spray.infinity.main.Infinity;

import net.minecraft.client.render.Camera;

@Mixin(Camera.class)
public class CameraMixin {

	@Inject(method = "clipToSpace", at = @At("HEAD"), cancellable = true)
	private void onClipToSpace(double desiredCameraDistance, CallbackInfoReturnable<Double> info) {
		if (Infinity.getModuleManager().get(CameraClip.class).isEnabled()) {
			info.setReturnValue(desiredCameraDistance);
		}
	}

}
