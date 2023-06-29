package net.minecraft.stats;

import net.minecraft.util.text.Component;

public class StatBasic extends StatBase
{
    public StatBasic(String statIdIn, Component statNameIn, IStatType typeIn)
    {
        super(statIdIn, statNameIn, typeIn);
    }

    public StatBasic(String statIdIn, Component statNameIn)
    {
        super(statIdIn, statNameIn);
    }

    /**
     * Register the stat into StatList.
     */
    public StatBase registerStat()
    {
        super.registerStat();
        StatList.BASIC_STATS.add(this);
        return this;
    }
}
