package org.spray.infinity.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spray.infinity.event.RenderTooltipEvent;

import com.darkmagician6.eventapi.EventManager;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;

@Mixin(Screen.class)
public class ScreenMixin {
	
	@Unique private boolean skipTooltip = false;
	
	@Inject(method = "renderOrderedTooltip", at = @At("HEAD"), cancellable = true)
	private void onRenderOrderedTooltip(MatrixStack matrices, List<? extends OrderedText> lines, int x, int y, CallbackInfo ci) {
		if (skipTooltip) {
			skipTooltip = false;
			return;
		}
		
		RenderTooltipEvent tooltipEvent = new RenderTooltipEvent(matrices, lines, x, y);
		EventManager.call(tooltipEvent);
		
		if (tooltipEvent.isCancelled()) {
			skipTooltip = true;
			tooltipEvent.getScreen().renderOrderedTooltip(matrices, tooltipEvent.getText(), tooltipEvent.getX(), tooltipEvent.getY());
			tooltipEvent.cancel();
		}
	}

}
