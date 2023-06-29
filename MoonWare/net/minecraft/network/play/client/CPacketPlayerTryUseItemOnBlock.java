package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class CPacketPlayerTryUseItemOnBlock implements Packet<INetHandlerPlayServer>
{
    private BlockPos position;
    private EnumFacing placedBlockDirection;
    private EnumHand hand;
    private float facingX;
    private float facingY;
    private float facingZ;

    public CPacketPlayerTryUseItemOnBlock()
    {
    }

    public CPacketPlayerTryUseItemOnBlock(BlockPos posIn, EnumFacing placedBlockDirectionIn, EnumHand handIn, float facingXIn, float facingYIn, float facingZIn)
    {
        position = posIn;
        placedBlockDirection = placedBlockDirectionIn;
        hand = handIn;
        facingX = facingXIn;
        facingY = facingYIn;
        facingZ = facingZIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        position = buf.readBlockPos();
        placedBlockDirection = buf.readEnumValue(EnumFacing.class);
        hand = buf.readEnumValue(EnumHand.class);
        facingX = buf.readFloat();
        facingY = buf.readFloat();
        facingZ = buf.readFloat();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeBlockPos(position);
        buf.writeEnumValue(placedBlockDirection);
        buf.writeEnumValue(hand);
        buf.writeFloat(facingX);
        buf.writeFloat(facingY);
        buf.writeFloat(facingZ);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processRightClickBlock(this);
    }

    public BlockPos getPos()
    {
        return position;
    }

    public EnumFacing getDirection()
    {
        return placedBlockDirection;
    }

    public EnumHand getHand()
    {
        return hand;
    }

    public float getFacingX()
    {
        return facingX;
    }

    public float getFacingY()
    {
        return facingY;
    }

    public float getFacingZ()
    {
        return facingZ;
    }
}
