package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.UUID;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.Component;
import net.minecraft.world.BossInfo;

public class SPacketUpdateBossInfo implements Packet<INetHandlerPlayClient>
{
    private UUID uniqueId;
    private SPacketUpdateBossInfo.Operation operation;
    private Component name;
    private float percent;
    private BossInfo.Color color;
    private BossInfo.Overlay overlay;
    private boolean darkenSky;
    private boolean playEndBossMusic;
    private boolean createFog;

    public SPacketUpdateBossInfo()
    {
    }

    public SPacketUpdateBossInfo(SPacketUpdateBossInfo.Operation operationIn, BossInfo data)
    {
        operation = operationIn;
        uniqueId = data.getUniqueId();
        name = data.getName();
        percent = data.getPercent();
        color = data.getColor();
        overlay = data.getOverlay();
        darkenSky = data.shouldDarkenSky();
        playEndBossMusic = data.shouldPlayEndBossMusic();
        createFog = data.shouldCreateFog();
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        uniqueId = buf.readUuid();
        operation = buf.readEnumValue(Operation.class);

        switch (operation)
        {
            case ADD:
                name = buf.readTextComponent();
                percent = buf.readFloat();
                color = buf.readEnumValue(BossInfo.Color.class);
                overlay = buf.readEnumValue(BossInfo.Overlay.class);
                setFlags(buf.readUnsignedByte());

            case REMOVE:
            default:
                break;

            case UPDATE_PCT:
                percent = buf.readFloat();
                break;

            case UPDATE_NAME:
                name = buf.readTextComponent();
                break;

            case UPDATE_STYLE:
                color = buf.readEnumValue(BossInfo.Color.class);
                overlay = buf.readEnumValue(BossInfo.Overlay.class);
                break;

            case UPDATE_PROPERTIES:
                setFlags(buf.readUnsignedByte());
        }
    }

    private void setFlags(int flags)
    {
        darkenSky = (flags & 1) > 0;
        playEndBossMusic = (flags & 2) > 0;
        createFog = (flags & 2) > 0;
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeUuid(uniqueId);
        buf.writeEnumValue(operation);

        switch (operation)
        {
            case ADD:
                buf.writeTextComponent(name);
                buf.writeFloat(percent);
                buf.writeEnumValue(color);
                buf.writeEnumValue(overlay);
                buf.writeByte(getFlags());

            case REMOVE:
            default:
                break;

            case UPDATE_PCT:
                buf.writeFloat(percent);
                break;

            case UPDATE_NAME:
                buf.writeTextComponent(name);
                break;

            case UPDATE_STYLE:
                buf.writeEnumValue(color);
                buf.writeEnumValue(overlay);
                break;

            case UPDATE_PROPERTIES:
                buf.writeByte(getFlags());
        }
    }

    private int getFlags()
    {
        int i = 0;

        if (darkenSky)
        {
            i |= 1;
        }

        if (playEndBossMusic)
        {
            i |= 2;
        }

        if (createFog)
        {
            i |= 2;
        }

        return i;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleUpdateEntityNBT(this);
    }

    public UUID getUniqueId()
    {
        return uniqueId;
    }

    public SPacketUpdateBossInfo.Operation getOperation()
    {
        return operation;
    }

    public Component getName()
    {
        return name;
    }

    public float getPercent()
    {
        return percent;
    }

    public BossInfo.Color getColor()
    {
        return color;
    }

    public BossInfo.Overlay getOverlay()
    {
        return overlay;
    }

    public boolean shouldDarkenSky()
    {
        return darkenSky;
    }

    public boolean shouldPlayEndBossMusic()
    {
        return playEndBossMusic;
    }

    public boolean shouldCreateFog()
    {
        return createFog;
    }

    public enum Operation
    {
        ADD,
        REMOVE,
        UPDATE_PCT,
        UPDATE_NAME,
        UPDATE_STYLE,
        UPDATE_PROPERTIES
    }
}
