package org.spray.infinity.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spray.infinity.features.module.visual.XRay;
import org.spray.infinity.main.Infinity;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {

	@Inject(method = "getAmbientOcclusionLightLevel", at = @At("HEAD"), cancellable = true)
	public void getAmbientOcclusionLightLevel(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1,
			CallbackInfoReturnable<Float> callbackInfoReturnable) {
		if (Infinity.getModuleManager().get(XRay.class).isEnabled()
				&& ((XRay) Infinity.getModuleManager().get(XRay.class)).isNoRender()) {
			callbackInfoReturnable.setReturnValue(1f);
		}
	}

}
