package net.minecraft.scoreboard;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.util.text.Formatting;

public class ScorePlayerTeam extends Team
{
    private final Scoreboard theScoreboard;
    private final String registeredName;
    private final Set<String> membershipSet = Sets.newHashSet();
    private String teamNameSPT;
    private String namePrefixSPT = "";
    private String colorSuffix = "";
    private boolean allowFriendlyFire = true;
    private boolean canSeeFriendlyInvisibles = true;
    private Team.EnumVisible nameTagVisibility = Team.EnumVisible.ALWAYS;
    private Team.EnumVisible deathMessageVisibility = Team.EnumVisible.ALWAYS;
    private Formatting chatFormat = Formatting.RESET;
    private Team.CollisionRule collisionRule = Team.CollisionRule.ALWAYS;

    public ScorePlayerTeam(Scoreboard theScoreboardIn, String name)
    {
        theScoreboard = theScoreboardIn;
        registeredName = name;
        teamNameSPT = name;
    }

    /**
     * Retrieve the name by which this team is registered in the scoreboard
     */
    public String getRegisteredName()
    {
        return registeredName;
    }

    public String getTeamName()
    {
        return teamNameSPT;
    }

    public void setTeamName(String name)
    {
        if (name == null)
        {
            throw new IllegalArgumentException("Name cannot be null");
        }
        else
        {
            teamNameSPT = name;
            theScoreboard.broadcastTeamInfoUpdate(this);
        }
    }

    public Collection<String> getMembershipCollection()
    {
        return membershipSet;
    }

    /**
     * Returns the color prefix for the player's team name
     */
    public String getColorPrefix()
    {
        return namePrefixSPT;
    }

    public void setNamePrefix(String prefix)
    {
        if (prefix == null)
        {
            throw new IllegalArgumentException("Prefix cannot be null");
        }
        else
        {
            namePrefixSPT = prefix;
            theScoreboard.broadcastTeamInfoUpdate(this);
        }
    }

    /**
     * Returns the color suffix for the player's team name
     */
    public String getColorSuffix()
    {
        return colorSuffix;
    }

    public void setNameSuffix(String suffix)
    {
        colorSuffix = suffix;
        theScoreboard.broadcastTeamInfoUpdate(this);
    }

    public String formatString(String input)
    {
        return getColorPrefix() + input + getColorSuffix();
    }

    /**
     * Returns the player name including the color prefixes and suffixes
     */
    public static String formatPlayerName(@Nullable Team teamIn, String string)
    {
        return teamIn == null ? string : teamIn.formatString(string);
    }

    public boolean getAllowFriendlyFire()
    {
        return allowFriendlyFire;
    }

    public void setAllowFriendlyFire(boolean friendlyFire)
    {
        allowFriendlyFire = friendlyFire;
        theScoreboard.broadcastTeamInfoUpdate(this);
    }

    public boolean getSeeFriendlyInvisiblesEnabled()
    {
        return canSeeFriendlyInvisibles;
    }

    public void setSeeFriendlyInvisiblesEnabled(boolean friendlyInvisibles)
    {
        canSeeFriendlyInvisibles = friendlyInvisibles;
        theScoreboard.broadcastTeamInfoUpdate(this);
    }

    public Team.EnumVisible getNameTagVisibility()
    {
        return nameTagVisibility;
    }

    public Team.EnumVisible getDeathMessageVisibility()
    {
        return deathMessageVisibility;
    }

    public void setNameTagVisibility(Team.EnumVisible visibility)
    {
        nameTagVisibility = visibility;
        theScoreboard.broadcastTeamInfoUpdate(this);
    }

    public void setDeathMessageVisibility(Team.EnumVisible visibility)
    {
        deathMessageVisibility = visibility;
        theScoreboard.broadcastTeamInfoUpdate(this);
    }

    public Team.CollisionRule getCollisionRule()
    {
        return collisionRule;
    }

    public void setCollisionRule(Team.CollisionRule rule)
    {
        collisionRule = rule;
        theScoreboard.broadcastTeamInfoUpdate(this);
    }

    public int getFriendlyFlags()
    {
        int i = 0;

        if (getAllowFriendlyFire())
        {
            i |= 1;
        }

        if (getSeeFriendlyInvisiblesEnabled())
        {
            i |= 2;
        }

        return i;
    }

    public void setFriendlyFlags(int flags)
    {
        setAllowFriendlyFire((flags & 1) > 0);
        setSeeFriendlyInvisiblesEnabled((flags & 2) > 0);
    }

    public void setChatFormat(Formatting format)
    {
        chatFormat = format;
    }

    public Formatting getChatFormat()
    {
        return chatFormat;
    }
}
