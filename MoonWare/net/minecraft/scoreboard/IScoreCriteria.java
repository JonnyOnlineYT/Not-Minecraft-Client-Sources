package net.minecraft.scoreboard;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.util.text.Formatting;

public interface IScoreCriteria
{
    Map<String, IScoreCriteria> INSTANCES = Maps.newHashMap();
    IScoreCriteria DUMMY = new ScoreCriteria("dummy");
    IScoreCriteria TRIGGER = new ScoreCriteria("trigger");
    IScoreCriteria DEATH_COUNT = new ScoreCriteria("deathCount");
    IScoreCriteria PLAYER_KILL_COUNT = new ScoreCriteria("playerKillCount");
    IScoreCriteria TOTAL_KILL_COUNT = new ScoreCriteria("totalKillCount");
    IScoreCriteria HEALTH = new ScoreCriteriaHealth("health");
    IScoreCriteria FOOD = new ScoreCriteriaReadOnly("food");
    IScoreCriteria AIR = new ScoreCriteriaReadOnly("air");
    IScoreCriteria ARMOR = new ScoreCriteriaReadOnly("armor");
    IScoreCriteria XP = new ScoreCriteriaReadOnly("xp");
    IScoreCriteria LEVEL = new ScoreCriteriaReadOnly("level");
    IScoreCriteria[] TEAM_KILL = {new ScoreCriteriaColored("teamkill.", Formatting.BLACK), new ScoreCriteriaColored("teamkill.", Formatting.DARK_BLUE), new ScoreCriteriaColored("teamkill.", Formatting.DARK_GREEN), new ScoreCriteriaColored("teamkill.", Formatting.DARK_AQUA), new ScoreCriteriaColored("teamkill.", Formatting.DARK_RED), new ScoreCriteriaColored("teamkill.", Formatting.DARK_PURPLE), new ScoreCriteriaColored("teamkill.", Formatting.GOLD), new ScoreCriteriaColored("teamkill.", Formatting.GRAY), new ScoreCriteriaColored("teamkill.", Formatting.DARK_GRAY), new ScoreCriteriaColored("teamkill.", Formatting.BLUE), new ScoreCriteriaColored("teamkill.", Formatting.GREEN), new ScoreCriteriaColored("teamkill.", Formatting.AQUA), new ScoreCriteriaColored("teamkill.", Formatting.RED), new ScoreCriteriaColored("teamkill.", Formatting.LIGHT_PURPLE), new ScoreCriteriaColored("teamkill.", Formatting.YELLOW), new ScoreCriteriaColored("teamkill.", Formatting.WHITE)};
    IScoreCriteria[] KILLED_BY_TEAM = {new ScoreCriteriaColored("killedByTeam.", Formatting.BLACK), new ScoreCriteriaColored("killedByTeam.", Formatting.DARK_BLUE), new ScoreCriteriaColored("killedByTeam.", Formatting.DARK_GREEN), new ScoreCriteriaColored("killedByTeam.", Formatting.DARK_AQUA), new ScoreCriteriaColored("killedByTeam.", Formatting.DARK_RED), new ScoreCriteriaColored("killedByTeam.", Formatting.DARK_PURPLE), new ScoreCriteriaColored("killedByTeam.", Formatting.GOLD), new ScoreCriteriaColored("killedByTeam.", Formatting.GRAY), new ScoreCriteriaColored("killedByTeam.", Formatting.DARK_GRAY), new ScoreCriteriaColored("killedByTeam.", Formatting.BLUE), new ScoreCriteriaColored("killedByTeam.", Formatting.GREEN), new ScoreCriteriaColored("killedByTeam.", Formatting.AQUA), new ScoreCriteriaColored("killedByTeam.", Formatting.RED), new ScoreCriteriaColored("killedByTeam.", Formatting.LIGHT_PURPLE), new ScoreCriteriaColored("killedByTeam.", Formatting.YELLOW), new ScoreCriteriaColored("killedByTeam.", Formatting.WHITE)};

    String getName();

    boolean isReadOnly();

    IScoreCriteria.EnumRenderType getRenderType();

    enum EnumRenderType
    {
        INTEGER("integer"),
        HEARTS("hearts");

        private static final Map<String, IScoreCriteria.EnumRenderType> BY_NAME = Maps.newHashMap();
        private final String renderType;

        EnumRenderType(String renderTypeIn)
        {
            renderType = renderTypeIn;
        }

        public String getRenderType()
        {
            return renderType;
        }

        public static IScoreCriteria.EnumRenderType getByName(String name)
        {
            IScoreCriteria.EnumRenderType iscorecriteria$enumrendertype = BY_NAME.get(name);
            return iscorecriteria$enumrendertype == null ? INTEGER : iscorecriteria$enumrendertype;
        }

        static {
            for (IScoreCriteria.EnumRenderType iscorecriteria$enumrendertype : values())
            {
                BY_NAME.put(iscorecriteria$enumrendertype.getRenderType(), iscorecriteria$enumrendertype);
            }
        }
    }
}
