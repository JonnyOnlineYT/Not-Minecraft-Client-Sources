package org.spray.infinity.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spray.infinity.event.ClickButtonEvent;

import com.darkmagician6.eventapi.EventManager;

import net.minecraft.client.Mouse;

@Mixin(Mouse.class)
public class MouseMixin {

	@Inject(method = { "onMouseButton" }, at = { @At("HEAD") }, cancellable = true)
	private void onMouseButton(long window, int button, int action, int mods, CallbackInfo info) {
		ClickButtonEvent clickEvent = new ClickButtonEvent(button);
		if (action == 1) {
			EventManager.call(clickEvent);
		}
		if (clickEvent.isCancelled())
			info.cancel();
	}

}
