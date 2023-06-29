package net.minecraft.network.play.server;

import java.io.IOException;

import com.google.common.base.Preconditions;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.SoundCategory;

public class SPacketCustomSound implements Packet<INetHandlerPlayClient>
{
    private String soundName;
    private SoundCategory category;
    private int x;
    private int y = Integer.MAX_VALUE;
    private int z;
    private float volume;
    private float pitch;

    public SPacketCustomSound()
    {
    }

    public SPacketCustomSound(String soundNameIn, SoundCategory categoryIn, double xIn, double yIn, double zIn, float volumeIn, float pitchIn)
    {
        Preconditions.checkNotNull(soundNameIn, "name");
        soundName = soundNameIn;
        category = categoryIn;
        x = (int)(xIn * 8.0D);
        y = (int)(yIn * 8.0D);
        z = (int)(zIn * 8.0D);
        volume = volumeIn;
        pitch = pitchIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        soundName = buf.readStringFromBuffer(256);
        category = buf.readEnumValue(SoundCategory.class);
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        volume = buf.readFloat();
        pitch = buf.readFloat();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeString(soundName);
        buf.writeEnumValue(category);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeFloat(volume);
        buf.writeFloat(pitch);
    }

    public String getSoundName()
    {
        return soundName;
    }

    public SoundCategory getCategory()
    {
        return category;
    }

    public double getX()
    {
        return (float) x / 8.0F;
    }

    public double getY()
    {
        return (float) y / 8.0F;
    }

    public double getZ()
    {
        return (float) z / 8.0F;
    }

    public float getVolume()
    {
        return volume;
    }

    public float getPitch()
    {
        return pitch;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleCustomSound(this);
    }
}
