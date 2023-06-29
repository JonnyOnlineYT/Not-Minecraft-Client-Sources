package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketSetPassengers implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private int[] passengerIds;

    public SPacketSetPassengers()
    {
    }

    public SPacketSetPassengers(Entity entityIn)
    {
        entityId = entityIn.getEntityId();
        List<Entity> list = entityIn.getPassengers();
        passengerIds = new int[list.size()];

        for (int i = 0; i < list.size(); ++i)
        {
            passengerIds[i] = list.get(i).getEntityId();
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        entityId = buf.readVarIntFromBuffer();
        passengerIds = buf.readVarIntArray();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(entityId);
        buf.writeVarIntArray(passengerIds);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleSetPassengers(this);
    }

    public int[] getPassengerIds()
    {
        return passengerIds;
    }

    public int getEntityId()
    {
        return entityId;
    }
}
