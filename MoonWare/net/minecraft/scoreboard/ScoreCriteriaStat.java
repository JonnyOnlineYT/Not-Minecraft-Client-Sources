package net.minecraft.scoreboard;

import net.minecraft.stats.StatBase;

public class ScoreCriteriaStat extends ScoreCriteria
{
    private final StatBase stat;

    public ScoreCriteriaStat(StatBase statIn)
    {
        super(statIn.statId);
        stat = statIn;
    }
}
