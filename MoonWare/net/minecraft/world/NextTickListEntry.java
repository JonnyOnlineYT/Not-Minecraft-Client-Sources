package net.minecraft.world;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public class NextTickListEntry implements Comparable<NextTickListEntry>
{
    /** The id number for the next tick entry */
    private static long nextTickEntryID;
    private final Block block;
    public final BlockPos position;

    /** Time this tick is scheduled to occur at */
    public long scheduledTime;
    public int priority;

    /** The id of the tick entry */
    private final long tickEntryID;

    public NextTickListEntry(BlockPos positionIn, Block blockIn)
    {
        tickEntryID = nextTickEntryID++;
        position = positionIn.toImmutable();
        block = blockIn;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (!(p_equals_1_ instanceof NextTickListEntry))
        {
            return false;
        }
        else
        {
            NextTickListEntry nextticklistentry = (NextTickListEntry)p_equals_1_;
            return position.equals(nextticklistentry.position) && Block.isEqualTo(block, nextticklistentry.block);
        }
    }

    public int hashCode()
    {
        return position.hashCode();
    }

    /**
     * Sets the scheduled time for this tick entry
     */
    public NextTickListEntry setScheduledTime(long scheduledTimeIn)
    {
        scheduledTime = scheduledTimeIn;
        return this;
    }

    public void setPriority(int priorityIn)
    {
        priority = priorityIn;
    }

    public int compareTo(NextTickListEntry p_compareTo_1_)
    {
        if (scheduledTime < p_compareTo_1_.scheduledTime)
        {
            return -1;
        }
        else if (scheduledTime > p_compareTo_1_.scheduledTime)
        {
            return 1;
        }
        else if (priority != p_compareTo_1_.priority)
        {
            return priority - p_compareTo_1_.priority;
        }
        else if (tickEntryID < p_compareTo_1_.tickEntryID)
        {
            return -1;
        }
        else
        {
            return tickEntryID > p_compareTo_1_.tickEntryID ? 1 : 0;
        }
    }

    public String toString()
    {
        return Block.getIdFromBlock(block) + ": " + position + ", " + scheduledTime + ", " + priority + ", " + tickEntryID;
    }

    public Block getBlock()
    {
        return block;
    }
}
