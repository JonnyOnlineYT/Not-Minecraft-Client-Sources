package org.spray.infinity.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spray.infinity.via.handler.CommonTransformer;
import org.spray.infinity.via.handler.FabricDecodeHandler;
import org.spray.infinity.via.handler.FabricEncodeHandler;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.connection.UserConnectionImpl;
import com.viaversion.viaversion.protocol.ProtocolPipelineImpl;

import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;

@Mixin(targets = "net.minecraft.server.ServerNetworkIo$1")
public class ServerNetworkIoChInitMixin {
	
    @Inject(method = "initChannel", at = @At(value = "TAIL"), remap = false)
    private void onInitChannel(Channel channel, CallbackInfo ci) {
        if (channel instanceof SocketChannel) {
            UserConnection user = new UserConnectionImpl(channel);
            new ProtocolPipelineImpl(user);

            channel.pipeline().addBefore("encoder", CommonTransformer.HANDLER_ENCODER_NAME, new FabricEncodeHandler(user));
            channel.pipeline().addBefore("decoder", CommonTransformer.HANDLER_DECODER_NAME, new FabricDecodeHandler(user));
        }
    }

}
