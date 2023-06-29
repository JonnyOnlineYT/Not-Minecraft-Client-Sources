package org.spray.infinity.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;

@Mixin(CloseHandledScreenC2SPacket.class)
public interface ICloseHandledScreenC2SPacket {

	@Accessor("syncId")
	int getSyncId();

}
