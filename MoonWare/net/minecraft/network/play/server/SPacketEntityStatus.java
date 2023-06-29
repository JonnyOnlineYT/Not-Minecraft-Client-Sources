package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;

public class SPacketEntityStatus implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private byte logicOpcode;

    public SPacketEntityStatus()
    {
    }

    public SPacketEntityStatus(Entity entityIn, byte opcodeIn)
    {
        entityId = entityIn.getEntityId();
        logicOpcode = opcodeIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        entityId = buf.readInt();
        logicOpcode = buf.readByte();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeInt(entityId);
        buf.writeByte(logicOpcode);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleEntityStatus(this);
    }

    public int getEntityId() {
        return entityId;
    }

    public Entity getEntity(World worldIn)
    {
        return worldIn.getEntityByID(entityId);
    }

    public byte getOpCode()
    {
        return logicOpcode;
    }
}
