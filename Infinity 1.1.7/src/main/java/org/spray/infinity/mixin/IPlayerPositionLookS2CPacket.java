package org.spray.infinity.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

@Mixin(PlayerPositionLookS2CPacket.class)
public interface IPlayerPositionLookS2CPacket {
	
	@Accessor("yaw")
	void setYaw(float yaw);
	
	@Accessor("pitch")
	void setPitch(float pitch);
	
	@Accessor("x")
	void setX(double x);
	
	@Accessor("y")
	void setY(double y);

	@Accessor("z")
	void setZ(double z);

}
