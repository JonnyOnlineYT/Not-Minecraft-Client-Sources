package org.spray.infinity.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spray.infinity.event.ClickEvent;
import org.spray.infinity.event.DisconnectEvent;
import org.spray.infinity.event.JoinWorldEvent;
import org.spray.infinity.event.OpenScreenEvent;
import org.spray.infinity.event.TickEvent;
import org.spray.infinity.main.Infinity;

import com.darkmagician6.eventapi.EventManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.profiler.Profiler;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

	@Shadow
	public ClientWorld world;

	@Shadow
	public abstract Profiler getProfiler();

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;onResolutionChanged()V", shift = At.Shift.AFTER))
	public void postInit(RunArgs runArgs, CallbackInfo ci) {
		Infinity.INSTANCE = new Infinity();
	}
	
	@Inject(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;printCrashReport(Lnet/minecraft/util/crash/CrashReport;)V"))
	public void crash(CallbackInfo info) {
		Infinity.INSTANCE.shutdown();
	}

	@Inject(method = "stop", at = @At("HEAD"))
	public void stop(CallbackInfo info) {
		Infinity.INSTANCE.shutdown();
	}

	@Inject(at = @At("HEAD"), method = "tick")
	private void tick(CallbackInfo info) {
		TickEvent tickEvent = new TickEvent();
		EventManager.call(tickEvent);
	}

	@Inject(at = {
			@At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;crosshairTarget:Lnet/minecraft/util/hit/HitResult;", ordinal = 0) }, method = {
					"doAttack()V" }, cancellable = true)
	private void onClick(CallbackInfo info) {
		ClickEvent clickEvent = new ClickEvent();
		EventManager.call(clickEvent);

		if (clickEvent.isCancelled())
			info.cancel();
	}

	@Inject(method = "openScreen", at = @At("HEAD"), cancellable = true)
	private void onOpenScreen(Screen screen, CallbackInfo ci) {
		OpenScreenEvent openScreenEvent = new OpenScreenEvent(screen);
		EventManager.call(openScreenEvent);
	}

	@Inject(method = "joinWorld", at = @At("TAIL"))
	public void onJoinWorld(ClientWorld world, CallbackInfo ci) {
		EventManager.call(new JoinWorldEvent(world));
	}

	@Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At("HEAD"), cancellable = true)
	public void onDisconnect(Screen screen, CallbackInfo ci) {
		DisconnectEvent disconnectEvent = new DisconnectEvent();
		EventManager.call(disconnectEvent);

		if (disconnectEvent.isCancelled())
			ci.cancel();
	}

}
