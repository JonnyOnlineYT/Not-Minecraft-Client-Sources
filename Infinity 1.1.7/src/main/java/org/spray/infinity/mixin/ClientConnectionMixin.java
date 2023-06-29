package org.spray.infinity.mixin;

import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spray.infinity.event.PacketEvent;
import org.spray.infinity.features.command.Command;
import org.spray.infinity.features.module.hidden.AntiFabric;
import org.spray.infinity.features.module.player.PacketKick;
import org.spray.infinity.main.Infinity;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.types.EventType;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

	@Shadow
	private Channel channel;

	@Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true)
	private static <T extends PacketListener> void onHandlePacket(Packet<T> packet, PacketListener listener,
			CallbackInfo info) {
		PacketEvent event = new PacketEvent(EventType.RECIEVE, packet);
		EventManager.call(event);

		if (event.isCancelled())
			info.cancel();
	}

	@Inject(method = "send(Lnet/minecraft/network/Packet;Lio/netty/util/concurrent/GenericFutureListener;)V", at = @At("HEAD"), cancellable = true)
	public void send(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>> genericFutureListener_1,
			CallbackInfo callback) {
		if (packet instanceof ChatMessageC2SPacket) {
			ChatMessageC2SPacket chatPacket = (ChatMessageC2SPacket) packet;

			// commands
			if (chatPacket.getChatMessage().startsWith(Command.prefix)) {
				Infinity.getCommandManager()
						.callCommand(chatPacket.getChatMessage().substring(Command.prefix.length()));
				callback.cancel();
			}
		}
		// antifabric spoof
		if (Infinity.getModuleManager().get(AntiFabric.class).isEnabled()) {
			if (!(packet instanceof ICustomPayloadC2SPacket))
				return;

			ICustomPayloadC2SPacket plPacket = (ICustomPayloadC2SPacket) packet;

			if (plPacket.getChannel().getNamespace().equals("minecraft")
					&& plPacket.getChannel().getPath().equals("register"))
				callback.cancel();

			if (plPacket.getChannel().getNamespace().equals("fabric"))
				callback.cancel();
		}

		PacketEvent sendEvent = new PacketEvent(EventType.SEND, packet);
		EventManager.call(sendEvent);

		if (sendEvent.isCancelled()) {
			callback.cancel();
		}
	}

	@ModifyVariable(method = "send(Lnet/minecraft/network/Packet;Lio/netty/util/concurrent/GenericFutureListener;)V", at = @At("HEAD"))
	public Packet<?> onSendPacket(Packet<?> packet) {
		if (Infinity.getModuleManager().get(AntiFabric.class).isEnabled()) {
			if ((packet instanceof ICustomPayloadC2SPacket)) {

				ICustomPayloadC2SPacket plPacket = (ICustomPayloadC2SPacket) packet;

				if (plPacket.getChannel().getNamespace().equals("minecraft")
						&& plPacket.getChannel().getPath().equals("brand"))
					packet = new CustomPayloadC2SPacket(CustomPayloadC2SPacket.BRAND,
							new PacketByteBuf(Unpooled.buffer()).writeString("vanilla"));
			}
		}

		return packet;
	}

	@Inject(method = "exceptionCaught", at = @At("HEAD"), cancellable = true)
	private void exceptionCaught(ChannelHandlerContext context, Throwable throwable, CallbackInfo ci) {
		if (throwable instanceof IOException && Infinity.getModuleManager().get(PacketKick.class).isEnabled())
			ci.cancel();
	}
}
