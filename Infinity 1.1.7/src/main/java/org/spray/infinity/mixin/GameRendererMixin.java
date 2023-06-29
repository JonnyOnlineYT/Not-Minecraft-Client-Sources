package org.spray.infinity.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spray.infinity.features.module.visual.NoHurtCam;
import org.spray.infinity.main.Infinity;

import net.minecraft.client.render.GameRenderer;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

	@Inject(method = "bobViewWhenHurt", at = @At("HEAD"), cancellable = true)
	public void onHurtCam(CallbackInfo ci) {
		if (Infinity.getModuleManager().get(NoHurtCam.class).isEnabled())
			ci.cancel();
	}
}
