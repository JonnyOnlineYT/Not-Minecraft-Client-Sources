package net.minecraft.network.play.server;

import java.io.IOException;

import com.google.common.base.Preconditions;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class SPacketSoundEffect implements Packet<INetHandlerPlayClient>
{
    private SoundEvent sound;
    private SoundCategory category;
    private int posX;
    private int posY;
    private int posZ;
    private float soundVolume;
    private float soundPitch;

    public SPacketSoundEffect()
    {
    }

    public SPacketSoundEffect(SoundEvent soundIn, SoundCategory categoryIn, double xIn, double yIn, double zIn, float volumeIn, float pitchIn)
    {
        Preconditions.checkNotNull(soundIn, "sound");
        sound = soundIn;
        category = categoryIn;
        posX = (int)(xIn * 8.0D);
        posY = (int)(yIn * 8.0D);
        posZ = (int)(zIn * 8.0D);
        soundVolume = volumeIn;
        soundPitch = pitchIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        sound = SoundEvent.REGISTRY.getObjectById(buf.readVarIntFromBuffer());
        category = buf.readEnumValue(SoundCategory.class);
        posX = buf.readInt();
        posY = buf.readInt();
        posZ = buf.readInt();
        soundVolume = buf.readFloat();
        soundPitch = buf.readFloat();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(SoundEvent.REGISTRY.getIDForObject(sound));
        buf.writeEnumValue(category);
        buf.writeInt(posX);
        buf.writeInt(posY);
        buf.writeInt(posZ);
        buf.writeFloat(soundVolume);
        buf.writeFloat(soundPitch);
    }

    public SoundEvent getSound()
    {
        return sound;
    }

    public SoundCategory getCategory()
    {
        return category;
    }

    public double getX()
    {
        return (float) posX / 8.0F;
    }

    public double getY()
    {
        return (float) posY / 8.0F;
    }

    public double getZ()
    {
        return (float) posZ / 8.0F;
    }

    public float getVolume()
    {
        return soundVolume;
    }

    public float getPitch()
    {
        return soundPitch;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleSoundEffect(this);
    }
}
