package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntityMetadata implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private List < EntityDataManager.DataEntry<? >> dataManagerEntries;

    public SPacketEntityMetadata()
    {
    }

    public SPacketEntityMetadata(int entityIdIn, EntityDataManager dataManagerIn, boolean sendAll)
    {
        entityId = entityIdIn;

        if (sendAll)
        {
            dataManagerEntries = dataManagerIn.getAll();
            dataManagerIn.setClean();
        }
        else
        {
            dataManagerEntries = dataManagerIn.getDirty();
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        entityId = buf.readVarIntFromBuffer();
        dataManagerEntries = EntityDataManager.readEntries(buf);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(entityId);
        EntityDataManager.writeEntries(dataManagerEntries, buf);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleEntityMetadata(this);
    }

    public List < EntityDataManager.DataEntry<? >> getDataManagerEntries()
    {
        return dataManagerEntries;
    }

    public int getEntityId()
    {
        return entityId;
    }
}
