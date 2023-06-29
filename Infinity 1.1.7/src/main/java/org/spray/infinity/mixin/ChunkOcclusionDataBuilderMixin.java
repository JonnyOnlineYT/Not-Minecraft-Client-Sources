package org.spray.infinity.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spray.infinity.features.module.visual.XRay;
import org.spray.infinity.main.Infinity;

import net.minecraft.client.render.chunk.ChunkOcclusionDataBuilder;
import net.minecraft.util.math.BlockPos;

@Mixin(ChunkOcclusionDataBuilder.class)
public class ChunkOcclusionDataBuilderMixin {

	@Inject(method = "markClosed", at = @At("HEAD"), cancellable = true)
	public void markClosed(BlockPos pos, CallbackInfo callback) {
		if (Infinity.getModuleManager().get(XRay.class).isEnabled()
				&& ((XRay) Infinity.getModuleManager().get(XRay.class)).isNoRender()) {
			callback.cancel();
		}
	}

}
