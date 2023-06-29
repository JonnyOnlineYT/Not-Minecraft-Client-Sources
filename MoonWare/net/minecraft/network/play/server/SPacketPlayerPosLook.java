package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketPlayerPosLook implements Packet<INetHandlerPlayClient>
{
    private double x;
    private double y;
    private double z;
    public float yaw;
    public float pitch;
    private Set<SPacketPlayerPosLook.EnumFlags> flags;
    private int teleportId;

    public SPacketPlayerPosLook()
    {
    }

    public SPacketPlayerPosLook(double xIn, double yIn, double zIn, float yawIn, float pitchIn, Set<SPacketPlayerPosLook.EnumFlags> flagsIn, int teleportIdIn)
    {
        x = xIn;
        y = yIn;
        z = zIn;
        yaw = yawIn;
        pitch = pitchIn;
        flags = flagsIn;
        teleportId = teleportIdIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
        yaw = buf.readFloat();
        pitch = buf.readFloat();
        flags = SPacketPlayerPosLook.EnumFlags.unpack(buf.readUnsignedByte());
        teleportId = buf.readVarIntFromBuffer();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeFloat(yaw);
        buf.writeFloat(pitch);
        buf.writeByte(SPacketPlayerPosLook.EnumFlags.pack(flags));
        buf.writeVarIntToBuffer(teleportId);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handlePlayerPosLook(this);
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public double getZ()
    {
        return z;
    }

    public float getYaw()
    {
        return yaw;
    }

    public float getPitch()
    {
        return pitch;
    }

    public int getTeleportId()
    {
        return teleportId;
    }

    public Set<SPacketPlayerPosLook.EnumFlags> getFlags()
    {
        return flags;
    }

    public enum EnumFlags
    {
        X(0),
        Y(1),
        Z(2),
        Y_ROT(3),
        X_ROT(4);

        private final int bit;

        EnumFlags(int p_i46690_3_)
        {
            bit = p_i46690_3_;
        }

        private int getMask()
        {
            return 1 << bit;
        }

        private boolean isSet(int p_187043_1_)
        {
            return (p_187043_1_ & getMask()) == getMask();
        }

        public static Set<SPacketPlayerPosLook.EnumFlags> unpack(int flags)
        {
            Set<SPacketPlayerPosLook.EnumFlags> set = EnumSet.noneOf(SPacketPlayerPosLook.EnumFlags.class);

            for (SPacketPlayerPosLook.EnumFlags spacketplayerposlook$enumflags : values())
            {
                if (spacketplayerposlook$enumflags.isSet(flags))
                {
                    set.add(spacketplayerposlook$enumflags);
                }
            }

            return set;
        }

        public static int pack(Set<SPacketPlayerPosLook.EnumFlags> flags)
        {
            int i = 0;

            for (SPacketPlayerPosLook.EnumFlags spacketplayerposlook$enumflags : flags)
            {
                i |= spacketplayerposlook$enumflags.getMask();
            }

            return i;
        }
    }
}
