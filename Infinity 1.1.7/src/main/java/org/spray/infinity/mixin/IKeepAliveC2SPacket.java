package org.spray.infinity.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.network.packet.c2s.play.KeepAliveC2SPacket;

@Mixin(KeepAliveC2SPacket.class)
public interface IKeepAliveC2SPacket {

	@Accessor("id")
	void setID(long id);
	
}
