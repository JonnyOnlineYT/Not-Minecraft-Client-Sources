package net.minecraft.block;

import net.minecraft.util.math.BlockPos;

public class BlockEventData
{
    private final BlockPos position;
    private final Block blockType;

    /** Different for each blockID */
    private final int eventID;
    private final int eventParameter;

    public BlockEventData(BlockPos pos, Block blockType, int eventId, int p_i45756_4_)
    {
        position = pos;
        eventID = eventId;
        eventParameter = p_i45756_4_;
        this.blockType = blockType;
    }

    public BlockPos getPosition()
    {
        return position;
    }

    /**
     * Get the Event ID (different for each BlockID)
     */
    public int getEventID()
    {
        return eventID;
    }

    public int getEventParameter()
    {
        return eventParameter;
    }

    public Block getBlock()
    {
        return blockType;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (!(p_equals_1_ instanceof BlockEventData))
        {
            return false;
        }
        else
        {
            BlockEventData blockeventdata = (BlockEventData)p_equals_1_;
            return position.equals(blockeventdata.position) && eventID == blockeventdata.eventID && eventParameter == blockeventdata.eventParameter && blockType == blockeventdata.blockType;
        }
    }

    public String toString()
    {
        return "TE(" + position + ")," + eventID + "," + eventParameter + "," + blockType;
    }
}
