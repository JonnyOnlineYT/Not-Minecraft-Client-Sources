package org.spray.infinity.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;

@Mixin(ExplosionS2CPacket.class)
public interface IExplosionS2CPacket {
	
	@Accessor("playerVelocityX")
	void setX(float x);
	
	@Accessor("playerVelocityY")
	void setY(float y);
	
	@Accessor("playerVelocityZ")
	void setZ(float z);

}
