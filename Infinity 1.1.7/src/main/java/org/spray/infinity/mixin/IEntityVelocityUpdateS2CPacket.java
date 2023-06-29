package org.spray.infinity.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;

@Mixin(EntityVelocityUpdateS2CPacket.class)
public interface IEntityVelocityUpdateS2CPacket {

	@Accessor("velocityX")
	void setVelocityX(int velocityX);
	
	@Accessor("velocityY")
	void setVelocityY(int velocityY);
	
	@Accessor("velocityZ")
	void setVelocityZ(int velocityZ);

}
