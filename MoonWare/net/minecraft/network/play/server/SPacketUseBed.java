package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SPacketUseBed implements Packet<INetHandlerPlayClient>
{
    private int playerID;

    /** Block location of the head part of the bed */
    private BlockPos bedPos;

    public SPacketUseBed()
    {
    }

    public SPacketUseBed(EntityPlayer player, BlockPos posIn)
    {
        playerID = player.getEntityId();
        bedPos = posIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        playerID = buf.readVarIntFromBuffer();
        bedPos = buf.readBlockPos();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(playerID);
        buf.writeBlockPos(bedPos);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleUseBed(this);
    }

    public EntityPlayer getPlayer(World worldIn)
    {
        return (EntityPlayer)worldIn.getEntityByID(playerID);
    }

    public BlockPos getBedPosition()
    {
        return bedPos;
    }
}
