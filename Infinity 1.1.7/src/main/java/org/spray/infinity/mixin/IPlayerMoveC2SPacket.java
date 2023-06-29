package org.spray.infinity.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

@Mixin(PlayerMoveC2SPacket.class)
public interface IPlayerMoveC2SPacket {
	
	@Accessor("yaw")
    void setYaw(float yaw);
	
	@Accessor("pitch")
    void setPitch(float yaw);
	
	@Accessor("x")
    void setX(double x);

	@Accessor("y")
    void setY(double y);
	
	@Accessor("z")
    void setZ(double z);
	
	@Accessor("onGround")
    void setOnGround(boolean onGround);
	
	@Accessor("changeLook")
	void setLook(boolean changeLook);
	
	@Accessor("changePosition")
	void setPos(boolean changePosition);

}
