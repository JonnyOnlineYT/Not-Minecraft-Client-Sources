package net.minecraft.scoreboard;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.text.Formatting;
import net.minecraft.world.storage.WorldSavedData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ScoreboardSaveData extends WorldSavedData
{
    private static final Logger LOGGER = LogManager.getLogger();
    private Scoreboard theScoreboard;
    private NBTTagCompound delayedInitNbt;

    public ScoreboardSaveData()
    {
        this("scoreboard");
    }

    public ScoreboardSaveData(String name)
    {
        super(name);
    }

    public void setScoreboard(Scoreboard scoreboardIn)
    {
        theScoreboard = scoreboardIn;

        if (delayedInitNbt != null)
        {
            readFromNBT(delayedInitNbt);
        }
    }

    /**
     * reads in data from the NBTTagCompound into this MapDataBase
     */
    public void readFromNBT(NBTTagCompound nbt)
    {
        if (theScoreboard == null)
        {
            delayedInitNbt = nbt;
        }
        else
        {
            readObjectives(nbt.getTagList("Objectives", 10));
            readScores(nbt.getTagList("PlayerScores", 10));

            if (nbt.hasKey("DisplaySlots", 10))
            {
                readDisplayConfig(nbt.getCompoundTag("DisplaySlots"));
            }

            if (nbt.hasKey("Teams", 9))
            {
                readTeams(nbt.getTagList("Teams", 10));
            }
        }
    }

    protected void readTeams(NBTTagList tagList)
    {
        for (int i = 0; i < tagList.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
            String s = nbttagcompound.getString("Name");

            if (s.length() > 16)
            {
                s = s.substring(0, 16);
            }

            ScorePlayerTeam scoreplayerteam = theScoreboard.createTeam(s);
            String s1 = nbttagcompound.getString("DisplayName");

            if (s1.length() > 32)
            {
                s1 = s1.substring(0, 32);
            }

            scoreplayerteam.setTeamName(s1);

            if (nbttagcompound.hasKey("TeamColor", 8))
            {
                scoreplayerteam.setChatFormat(Formatting.getValueByName(nbttagcompound.getString("TeamColor")));
            }

            scoreplayerteam.setNamePrefix(nbttagcompound.getString("Prefix"));
            scoreplayerteam.setNameSuffix(nbttagcompound.getString("Suffix"));

            if (nbttagcompound.hasKey("AllowFriendlyFire", 99))
            {
                scoreplayerteam.setAllowFriendlyFire(nbttagcompound.getBoolean("AllowFriendlyFire"));
            }

            if (nbttagcompound.hasKey("SeeFriendlyInvisibles", 99))
            {
                scoreplayerteam.setSeeFriendlyInvisiblesEnabled(nbttagcompound.getBoolean("SeeFriendlyInvisibles"));
            }

            if (nbttagcompound.hasKey("NameTagVisibility", 8))
            {
                Team.EnumVisible team$enumvisible = Team.EnumVisible.getByName(nbttagcompound.getString("NameTagVisibility"));

                if (team$enumvisible != null)
                {
                    scoreplayerteam.setNameTagVisibility(team$enumvisible);
                }
            }

            if (nbttagcompound.hasKey("DeathMessageVisibility", 8))
            {
                Team.EnumVisible team$enumvisible1 = Team.EnumVisible.getByName(nbttagcompound.getString("DeathMessageVisibility"));

                if (team$enumvisible1 != null)
                {
                    scoreplayerteam.setDeathMessageVisibility(team$enumvisible1);
                }
            }

            if (nbttagcompound.hasKey("CollisionRule", 8))
            {
                Team.CollisionRule team$collisionrule = Team.CollisionRule.getByName(nbttagcompound.getString("CollisionRule"));

                if (team$collisionrule != null)
                {
                    scoreplayerteam.setCollisionRule(team$collisionrule);
                }
            }

            loadTeamPlayers(scoreplayerteam, nbttagcompound.getTagList("Players", 8));
        }
    }

    protected void loadTeamPlayers(ScorePlayerTeam playerTeam, NBTTagList tagList)
    {
        for (int i = 0; i < tagList.tagCount(); ++i)
        {
            theScoreboard.addPlayerToTeam(tagList.getStringTagAt(i), playerTeam.getRegisteredName());
        }
    }

    protected void readDisplayConfig(NBTTagCompound compound)
    {
        for (int i = 0; i < 19; ++i)
        {
            if (compound.hasKey("slot_" + i, 8))
            {
                String s = compound.getString("slot_" + i);
                ScoreObjective scoreobjective = theScoreboard.getObjective(s);
                theScoreboard.setObjectiveInDisplaySlot(i, scoreobjective);
            }
        }
    }

    protected void readObjectives(NBTTagList nbt)
    {
        for (int i = 0; i < nbt.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = nbt.getCompoundTagAt(i);
            IScoreCriteria iscorecriteria = IScoreCriteria.INSTANCES.get(nbttagcompound.getString("CriteriaName"));

            if (iscorecriteria != null)
            {
                String s = nbttagcompound.getString("Name");

                if (s.length() > 16)
                {
                    s = s.substring(0, 16);
                }

                ScoreObjective scoreobjective = theScoreboard.addScoreObjective(s, iscorecriteria);
                scoreobjective.setDisplayName(nbttagcompound.getString("DisplayName"));
                scoreobjective.setRenderType(IScoreCriteria.EnumRenderType.getByName(nbttagcompound.getString("RenderType")));
            }
        }
    }

    protected void readScores(NBTTagList nbt)
    {
        for (int i = 0; i < nbt.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = nbt.getCompoundTagAt(i);
            ScoreObjective scoreobjective = theScoreboard.getObjective(nbttagcompound.getString("Objective"));
            String s = nbttagcompound.getString("Name");

            if (s.length() > 40)
            {
                s = s.substring(0, 40);
            }

            Score score = theScoreboard.getOrCreateScore(s, scoreobjective);
            score.setScorePoints(nbttagcompound.getInteger("Score"));

            if (nbttagcompound.hasKey("Locked"))
            {
                score.setLocked(nbttagcompound.getBoolean("Locked"));
            }
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        if (theScoreboard == null)
        {
            LOGGER.warn("Tried to save scoreboard without having a scoreboard...");
            return compound;
        }
        else
        {
            compound.setTag("Objectives", objectivesToNbt());
            compound.setTag("PlayerScores", scoresToNbt());
            compound.setTag("Teams", teamsToNbt());
            fillInDisplaySlots(compound);
            return compound;
        }
    }

    protected NBTTagList teamsToNbt()
    {
        NBTTagList nbttaglist = new NBTTagList();

        for (ScorePlayerTeam scoreplayerteam : theScoreboard.getTeams())
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setString("Name", scoreplayerteam.getRegisteredName());
            nbttagcompound.setString("DisplayName", scoreplayerteam.getTeamName());

            if (scoreplayerteam.getChatFormat().getIndex() >= 0)
            {
                nbttagcompound.setString("TeamColor", scoreplayerteam.getChatFormat().getName());
            }

            nbttagcompound.setString("Prefix", scoreplayerteam.getColorPrefix());
            nbttagcompound.setString("Suffix", scoreplayerteam.getColorSuffix());
            nbttagcompound.setBoolean("AllowFriendlyFire", scoreplayerteam.getAllowFriendlyFire());
            nbttagcompound.setBoolean("SeeFriendlyInvisibles", scoreplayerteam.getSeeFriendlyInvisiblesEnabled());
            nbttagcompound.setString("NameTagVisibility", scoreplayerteam.getNameTagVisibility().internalName);
            nbttagcompound.setString("DeathMessageVisibility", scoreplayerteam.getDeathMessageVisibility().internalName);
            nbttagcompound.setString("CollisionRule", scoreplayerteam.getCollisionRule().name);
            NBTTagList nbttaglist1 = new NBTTagList();

            for (String s : scoreplayerteam.getMembershipCollection())
            {
                nbttaglist1.appendTag(new NBTTagString(s));
            }

            nbttagcompound.setTag("Players", nbttaglist1);
            nbttaglist.appendTag(nbttagcompound);
        }

        return nbttaglist;
    }

    protected void fillInDisplaySlots(NBTTagCompound compound)
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        boolean flag = false;

        for (int i = 0; i < 19; ++i)
        {
            ScoreObjective scoreobjective = theScoreboard.getObjectiveInDisplaySlot(i);

            if (scoreobjective != null)
            {
                nbttagcompound.setString("slot_" + i, scoreobjective.getName());
                flag = true;
            }
        }

        if (flag)
        {
            compound.setTag("DisplaySlots", nbttagcompound);
        }
    }

    protected NBTTagList objectivesToNbt()
    {
        NBTTagList nbttaglist = new NBTTagList();

        for (ScoreObjective scoreobjective : theScoreboard.getScoreObjectives())
        {
            if (scoreobjective.getCriteria() != null)
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setString("Name", scoreobjective.getName());
                nbttagcompound.setString("CriteriaName", scoreobjective.getCriteria().getName());
                nbttagcompound.setString("DisplayName", scoreobjective.getDisplayName());
                nbttagcompound.setString("RenderType", scoreobjective.getRenderType().getRenderType());
                nbttaglist.appendTag(nbttagcompound);
            }
        }

        return nbttaglist;
    }

    protected NBTTagList scoresToNbt()
    {
        NBTTagList nbttaglist = new NBTTagList();

        for (Score score : theScoreboard.getScores())
        {
            if (score.getObjective() != null)
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setString("Name", score.getPlayerName());
                nbttagcompound.setString("Objective", score.getObjective().getName());
                nbttagcompound.setInteger("Score", score.getScorePoints());
                nbttagcompound.setBoolean("Locked", score.isLocked());
                nbttaglist.appendTag(nbttagcompound);
            }
        }

        return nbttaglist;
    }
}
