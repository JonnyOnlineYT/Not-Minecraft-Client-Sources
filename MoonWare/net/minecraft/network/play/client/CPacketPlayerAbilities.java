package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketPlayerAbilities implements Packet<INetHandlerPlayServer>
{
    private boolean invulnerable;
    private boolean flying;
    private boolean allowFlying;
    private boolean creativeMode;
    private float flySpeed;
    private float walkSpeed;

    public CPacketPlayerAbilities()
    {
    }

    public CPacketPlayerAbilities(PlayerCapabilities capabilities)
    {
        setInvulnerable(capabilities.disableDamage);
        setFlying(capabilities.isFlying);
        setAllowFlying(capabilities.allowFlying);
        setCreativeMode(capabilities.isCreativeMode);
        setFlySpeed(capabilities.getFlySpeed());
        setWalkSpeed(capabilities.getWalkSpeed());
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        byte b0 = buf.readByte();
        setInvulnerable((b0 & 1) > 0);
        setFlying((b0 & 2) > 0);
        setAllowFlying((b0 & 4) > 0);
        setCreativeMode((b0 & 8) > 0);
        setFlySpeed(buf.readFloat());
        setWalkSpeed(buf.readFloat());
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        byte b0 = 0;

        if (isInvulnerable())
        {
            b0 = (byte)(b0 | 1);
        }

        if (isFlying())
        {
            b0 = (byte)(b0 | 2);
        }

        if (isAllowFlying())
        {
            b0 = (byte)(b0 | 4);
        }

        if (isCreativeMode())
        {
            b0 = (byte)(b0 | 8);
        }

        buf.writeByte(b0);
        buf.writeFloat(flySpeed);
        buf.writeFloat(walkSpeed);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processPlayerAbilities(this);
    }

    public boolean isInvulnerable()
    {
        return invulnerable;
    }

    public void setInvulnerable(boolean isInvulnerable)
    {
        invulnerable = isInvulnerable;
    }

    public boolean isFlying()
    {
        return flying;
    }

    public void setFlying(boolean isFlying)
    {
        flying = isFlying;
    }

    public boolean isAllowFlying()
    {
        return allowFlying;
    }

    public void setAllowFlying(boolean isAllowFlying)
    {
        allowFlying = isAllowFlying;
    }

    public boolean isCreativeMode()
    {
        return creativeMode;
    }

    public void setCreativeMode(boolean isCreativeMode)
    {
        creativeMode = isCreativeMode;
    }

    public void setFlySpeed(float flySpeedIn)
    {
        flySpeed = flySpeedIn;
    }

    public void setWalkSpeed(float walkSpeedIn)
    {
        walkSpeed = walkSpeedIn;
    }
}
