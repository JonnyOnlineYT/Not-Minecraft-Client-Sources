package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.UUID;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class SPacketSpawnPainting implements Packet<INetHandlerPlayClient>
{
    private int entityID;
    private UUID uniqueId;
    private BlockPos position;
    private EnumFacing facing;
    private String title;

    public SPacketSpawnPainting()
    {
    }

    public SPacketSpawnPainting(EntityPainting painting)
    {
        entityID = painting.getEntityId();
        uniqueId = painting.getUniqueID();
        position = painting.getHangingPosition();
        facing = painting.facingDirection;
        title = painting.art.title;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        entityID = buf.readVarIntFromBuffer();
        uniqueId = buf.readUuid();
        title = buf.readStringFromBuffer(EntityPainting.EnumArt.MAX_NAME_LENGTH);
        position = buf.readBlockPos();
        facing = EnumFacing.getHorizontal(buf.readUnsignedByte());
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(entityID);
        buf.writeUuid(uniqueId);
        buf.writeString(title);
        buf.writeBlockPos(position);
        buf.writeByte(facing.getHorizontalIndex());
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleSpawnPainting(this);
    }

    public int getEntityID()
    {
        return entityID;
    }

    public UUID getUniqueId()
    {
        return uniqueId;
    }

    public BlockPos getPosition()
    {
        return position;
    }

    public EnumFacing getFacing()
    {
        return facing;
    }

    public String getTitle()
    {
        return title;
    }
}
