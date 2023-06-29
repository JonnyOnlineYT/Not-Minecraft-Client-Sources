package org.spray.infinity.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spray.infinity.event.AttackEvent;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

	@Inject(method = "attackEntity", at = @At("HEAD"), cancellable = true)
	public void preAttack(PlayerEntity entity, Entity target, CallbackInfo info) {
		AttackEvent attackEvent = new AttackEvent(EventType.PRE);
		EventManager.call(attackEvent);
		if (attackEvent.isCancelled())
			info.cancel();
	}

	@Inject(method = "attackEntity", at = @At("RETURN"), cancellable = true)
	public void postAttack(PlayerEntity entity, Entity target, CallbackInfo info) {
		AttackEvent attackEvent = new AttackEvent(EventType.POST);
		EventManager.call(attackEvent);
		if (attackEvent.isCancelled())
			info.cancel();
	}

}
