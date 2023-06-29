package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketInput implements Packet<INetHandlerPlayServer>
{
    /** Positive for left strafe, negative for right */
    private float strafeSpeed;
    private float field_192621_b;
    private boolean jumping;
    private boolean sneaking;

    public CPacketInput()
    {
    }

    public CPacketInput(float strafeSpeedIn, float forwardSpeedIn, boolean jumpingIn, boolean sneakingIn)
    {
        strafeSpeed = strafeSpeedIn;
        field_192621_b = forwardSpeedIn;
        jumping = jumpingIn;
        sneaking = sneakingIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        strafeSpeed = buf.readFloat();
        field_192621_b = buf.readFloat();
        byte b0 = buf.readByte();
        jumping = (b0 & 1) > 0;
        sneaking = (b0 & 2) > 0;
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeFloat(strafeSpeed);
        buf.writeFloat(field_192621_b);
        byte b0 = 0;

        if (jumping)
        {
            b0 = (byte)(b0 | 1);
        }

        if (sneaking)
        {
            b0 = (byte)(b0 | 2);
        }

        buf.writeByte(b0);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processInput(this);
    }

    public float getStrafeSpeed()
    {
        return strafeSpeed;
    }

    public float func_192620_b()
    {
        return field_192621_b;
    }

    public boolean isJumping()
    {
        return jumping;
    }

    public boolean isSneaking()
    {
        return sneaking;
    }
}
