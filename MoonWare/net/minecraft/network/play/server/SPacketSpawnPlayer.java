package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSpawnPlayer implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private UUID uniqueId;
    private double x;
    private double y;
    private double z;
    private byte yaw;
    private byte pitch;
    private EntityDataManager watcher;
    private List < EntityDataManager.DataEntry<? >> dataManagerEntries;

    public SPacketSpawnPlayer()
    {
    }

    public SPacketSpawnPlayer(EntityPlayer player)
    {
        entityId = player.getEntityId();
        uniqueId = player.getGameProfile().getId();
        x = player.posX;
        y = player.posY;
        z = player.posZ;
        yaw = (byte)((int)(player.rotationYaw * 256.0F / 360.0F));
        pitch = (byte)((int)(player.rotationPitch * 256.0F / 360.0F));
        watcher = player.getDataManager();
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        entityId = buf.readVarIntFromBuffer();
        uniqueId = buf.readUuid();
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
        yaw = buf.readByte();
        pitch = buf.readByte();
        dataManagerEntries = EntityDataManager.readEntries(buf);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(entityId);
        buf.writeUuid(uniqueId);
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeByte(yaw);
        buf.writeByte(pitch);
        watcher.writeEntries(buf);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleSpawnPlayer(this);
    }

    @Nullable
    public List < EntityDataManager.DataEntry<? >> getDataManagerEntries()
    {
        return dataManagerEntries;
    }

    public int getEntityID()
    {
        return entityId;
    }

    public UUID getUniqueId()
    {
        return uniqueId;
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

    public byte getYaw()
    {
        return yaw;
    }

    public byte getPitch()
    {
        return pitch;
    }
}
