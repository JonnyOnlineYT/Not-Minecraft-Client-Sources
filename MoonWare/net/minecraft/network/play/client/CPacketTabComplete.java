package net.minecraft.network.play.client;

import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.StringUtils;

public class CPacketTabComplete implements Packet<INetHandlerPlayServer>
{
    private String message;
    private boolean hasTargetBlock;
    @Nullable
    private BlockPos targetBlock;

    public CPacketTabComplete()
    {
    }

    public CPacketTabComplete(String messageIn, @Nullable BlockPos targetBlockIn, boolean hasTargetBlockIn)
    {
        message = messageIn;
        targetBlock = targetBlockIn;
        hasTargetBlock = hasTargetBlockIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        message = buf.readStringFromBuffer(32767);
        hasTargetBlock = buf.readBoolean();
        boolean flag = buf.readBoolean();

        if (flag)
        {
            targetBlock = buf.readBlockPos();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeString(StringUtils.substring(message, 0, 32767));
        buf.writeBoolean(hasTargetBlock);
        boolean flag = targetBlock != null;
        buf.writeBoolean(flag);

        if (flag)
        {
            buf.writeBlockPos(targetBlock);
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processTabComplete(this);
    }

    public String getMessage()
    {
        return message;
    }

    @Nullable
    public BlockPos getTargetBlock()
    {
        return targetBlock;
    }

    public boolean hasTargetBlock()
    {
        return hasTargetBlock;
    }
}
