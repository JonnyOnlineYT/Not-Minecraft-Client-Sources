package net.minecraft.scoreboard;

import net.minecraft.util.text.Formatting;

public class ScoreCriteriaColored implements IScoreCriteria
{
    private final String goalName;

    public ScoreCriteriaColored(String name, Formatting format)
    {
        goalName = name + format.getName();
        IScoreCriteria.INSTANCES.put(goalName, this);
    }

    public String getName()
    {
        return goalName;
    }

    public boolean isReadOnly()
    {
        return false;
    }

    public IScoreCriteria.EnumRenderType getRenderType()
    {
        return IScoreCriteria.EnumRenderType.INTEGER;
    }
}
