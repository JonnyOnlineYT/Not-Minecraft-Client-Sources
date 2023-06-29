package net.minecraft.scoreboard;

public class ScoreObjective
{
    private final Scoreboard theScoreboard;
    private final String name;

    /** The ScoreObjectiveCriteria for this objetive */
    private final IScoreCriteria objectiveCriteria;
    private IScoreCriteria.EnumRenderType renderType;
    private String displayName;

    public ScoreObjective(Scoreboard theScoreboardIn, String nameIn, IScoreCriteria objectiveCriteriaIn)
    {
        theScoreboard = theScoreboardIn;
        name = nameIn;
        objectiveCriteria = objectiveCriteriaIn;
        displayName = nameIn;
        renderType = objectiveCriteriaIn.getRenderType();
    }

    public Scoreboard getScoreboard()
    {
        return theScoreboard;
    }

    public String getName()
    {
        return name;
    }

    public IScoreCriteria getCriteria()
    {
        return objectiveCriteria;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String nameIn)
    {
        displayName = nameIn;
        theScoreboard.onObjectiveDisplayNameChanged(this);
    }

    public IScoreCriteria.EnumRenderType getRenderType()
    {
        return renderType;
    }

    public void setRenderType(IScoreCriteria.EnumRenderType type)
    {
        renderType = type;
        theScoreboard.onObjectiveDisplayNameChanged(this);
    }
}
