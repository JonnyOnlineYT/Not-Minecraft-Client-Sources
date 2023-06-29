package org.spray.infinity.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spray.infinity.event.ServerChatEvent;

import com.darkmagician6.eventapi.EventManager;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

	@Inject(method = "onGameMessage", at = @At("HEAD"), cancellable = true)
	private void gameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
		ServerChatEvent chatEvent = new ServerChatEvent(packet.getMessage().getString());
		EventManager.call(chatEvent);

		if (chatEvent.isCancelled())
			ci.cancel();
	}

}
