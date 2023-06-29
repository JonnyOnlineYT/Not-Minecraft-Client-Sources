package net.minecraft.scoreboard;

import java.util.Comparator;

public class Score
{
    public static final Comparator<Score> SCORE_COMPARATOR = new Comparator<Score>()
    {
        public int compare(Score p_compare_1_, Score p_compare_2_)
        {
            if (p_compare_1_.getScorePoints() > p_compare_2_.getScorePoints())
            {
                return 1;
            }
            else
            {
                return p_compare_1_.getScorePoints() < p_compare_2_.getScorePoints() ? -1 : p_compare_2_.getPlayerName().compareToIgnoreCase(p_compare_1_.getPlayerName());
            }
        }
    };
    private final Scoreboard theScoreboard;
    private final ScoreObjective theScoreObjective;
    private final String scorePlayerName;
    private int scorePoints;
    private boolean locked;
    private boolean forceUpdate;

    public Score(Scoreboard theScoreboardIn, ScoreObjective theScoreObjectiveIn, String scorePlayerNameIn)
    {
        theScoreboard = theScoreboardIn;
        theScoreObjective = theScoreObjectiveIn;
        scorePlayerName = scorePlayerNameIn;
        forceUpdate = true;
    }

    public void increaseScore(int amount)
    {
        if (theScoreObjective.getCriteria().isReadOnly())
        {
            throw new IllegalStateException("Cannot modify read-only score");
        }
        else
        {
            setScorePoints(getScorePoints() + amount);
        }
    }

    public void decreaseScore(int amount)
    {
        if (theScoreObjective.getCriteria().isReadOnly())
        {
            throw new IllegalStateException("Cannot modify read-only score");
        }
        else
        {
            setScorePoints(getScorePoints() - amount);
        }
    }

    public void incrementScore()
    {
        if (theScoreObjective.getCriteria().isReadOnly())
        {
            throw new IllegalStateException("Cannot modify read-only score");
        }
        else
        {
            increaseScore(1);
        }
    }

    public int getScorePoints()
    {
        return scorePoints;
    }

    public void setScorePoints(int points)
    {
        int i = scorePoints;
        scorePoints = points;

        if (i != points || forceUpdate)
        {
            forceUpdate = false;
            getScoreScoreboard().onScoreUpdated(this);
        }
    }

    public ScoreObjective getObjective()
    {
        return theScoreObjective;
    }

    /**
     * Returns the name of the player this score belongs to
     */
    public String getPlayerName()
    {
        return scorePlayerName;
    }

    public Scoreboard getScoreScoreboard()
    {
        return theScoreboard;
    }

    public boolean isLocked()
    {
        return locked;
    }

    public void setLocked(boolean locked)
    {
        this.locked = locked;
    }
}
