package org.spray.infinity.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spray.infinity.features.module.visual.XRay;
import org.spray.infinity.main.Infinity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

@Mixin(Block.class)
public class BlockMixin {

	@Inject(method = "shouldDrawSide", at = @At("HEAD"), cancellable = true)
	private static void shouldDrawSide(BlockState state, BlockView world, BlockPos pos, Direction side, BlockPos blockPos,
									   CallbackInfoReturnable<Boolean> callback) {
		XRay xray = ((XRay) Infinity.getModuleManager().get(XRay.class));
		if (xray.isEnabled() && xray.isNoRender()) {
			callback.setReturnValue(xray.isValid(state.getBlock()));
			callback.cancel();
		}
	}

	@Inject(method = "isShapeFullCube", at = @At("HEAD"), cancellable = true)
	private static void isShapeFullCube(VoxelShape shape, CallbackInfoReturnable<Boolean> callback) {
		if (Infinity.getModuleManager().get(XRay.class).isEnabled()
				&& ((XRay) Infinity.getModuleManager().get(XRay.class)).isNoRender()) {
			callback.setReturnValue(false);
		}
	}

}
