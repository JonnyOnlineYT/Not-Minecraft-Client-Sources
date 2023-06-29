package net.minecraft.stats;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.TupleIntJsonSerializable;

import java.util.Map;

public class StatisticsManager
{
    protected final Map<StatBase, TupleIntJsonSerializable> statsData = Maps.newConcurrentMap();

    public void increaseStat(EntityPlayer player, StatBase stat, int amount)
    {
        unlockAchievement(player, stat, readStat(stat) + amount);
    }

    /**
     * Triggers the logging of an achievement and attempts to announce to server
     */
    public void unlockAchievement(EntityPlayer playerIn, StatBase statIn, int p_150873_3_)
    {
        TupleIntJsonSerializable tupleintjsonserializable = statsData.get(statIn);

        if (tupleintjsonserializable == null)
        {
            tupleintjsonserializable = new TupleIntJsonSerializable();
            statsData.put(statIn, tupleintjsonserializable);
        }

        tupleintjsonserializable.setIntegerValue(p_150873_3_);
    }

    /**
     * Reads the given stat and returns its value as an int.
     */
    public int readStat(StatBase stat)
    {
        TupleIntJsonSerializable tupleintjsonserializable = statsData.get(stat);
        return tupleintjsonserializable == null ? 0 : tupleintjsonserializable.getIntegerValue();
    }
}
