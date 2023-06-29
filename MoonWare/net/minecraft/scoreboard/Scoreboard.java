package net.minecraft.scoreboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.Formatting;

public class Scoreboard
{
    private final Map<String, ScoreObjective> scoreObjectives = Maps.newHashMap();
    private final Map<IScoreCriteria, List<ScoreObjective>> scoreObjectiveCriterias = Maps.newHashMap();
    private final Map<String, Map<ScoreObjective, Score>> entitiesScoreObjectives = Maps.newHashMap();

    /** Index 0 is tab menu, 1 is sidebar, and 2 is below name */
    private final ScoreObjective[] objectiveDisplaySlots = new ScoreObjective[19];
    private final Map<String, ScorePlayerTeam> teams = Maps.newHashMap();
    private final Map<String, ScorePlayerTeam> teamMemberships = Maps.newHashMap();
    private static String[] displaySlots;

    @Nullable

    /**
     * Returns a ScoreObjective for the objective name
     */
    public ScoreObjective getObjective(String name)
    {
        return scoreObjectives.get(name);
    }

    /**
     * Create and returns the score objective for the given name and ScoreCriteria
     */
    public ScoreObjective addScoreObjective(String name, IScoreCriteria criteria)
    {
        if (name.length() > 16)
        {
            throw new IllegalArgumentException("The objective name '" + name + "' is too long!");
        }
        else
        {
            ScoreObjective scoreobjective = getObjective(name);

            if (scoreobjective != null)
            {
                throw new IllegalArgumentException("An objective with the name '" + name + "' already exists!");
            }
            else
            {
                scoreobjective = new ScoreObjective(this, name, criteria);
                List<ScoreObjective> list = scoreObjectiveCriterias.get(criteria);

                if (list == null)
                {
                    list = Lists.newArrayList();
                    scoreObjectiveCriterias.put(criteria, list);
                }

                list.add(scoreobjective);
                scoreObjectives.put(name, scoreobjective);
                onScoreObjectiveAdded(scoreobjective);
                return scoreobjective;
            }
        }
    }

    public Collection<ScoreObjective> getObjectivesFromCriteria(IScoreCriteria criteria)
    {
        Collection<ScoreObjective> collection = scoreObjectiveCriterias.get(criteria);
        return collection == null ? Lists.newArrayList() : Lists.newArrayList(collection);
    }

    /**
     * Returns if the entity has the given ScoreObjective
     */
    public boolean entityHasObjective(String name, ScoreObjective objective)
    {
        Map<ScoreObjective, Score> map = entitiesScoreObjectives.get(name);

        if (map == null)
        {
            return false;
        }
        else
        {
            Score score = map.get(objective);
            return score != null;
        }
    }

    /**
     * Get a player's score or create it if it does not exist
     */
    public Score getOrCreateScore(String username, ScoreObjective objective)
    {
        if (username.length() > 40)
        {
            throw new IllegalArgumentException("The player name '" + username + "' is too long!");
        }
        else
        {
            Map<ScoreObjective, Score> map = entitiesScoreObjectives.get(username);

            if (map == null)
            {
                map = Maps.newHashMap();
                entitiesScoreObjectives.put(username, map);
            }

            Score score = map.get(objective);

            if (score == null)
            {
                score = new Score(this, objective, username);
                map.put(objective, score);
            }

            return score;
        }
    }

    public Collection<Score> getSortedScores(ScoreObjective objective)
    {
        List<Score> list = Lists.newArrayList();

        for (Map<ScoreObjective, Score> map : entitiesScoreObjectives.values())
        {
            Score score = map.get(objective);

            if (score != null)
            {
                list.add(score);
            }
        }

        Collections.sort(list, Score.SCORE_COMPARATOR);
        return list;
    }

    public Collection<ScoreObjective> getScoreObjectives()
    {
        return scoreObjectives.values();
    }

    public Collection<String> getObjectiveNames()
    {
        return entitiesScoreObjectives.keySet();
    }

    /**
     * Remove the given ScoreObjective for the given Entity name.
     */
    public void removeObjectiveFromEntity(String name, ScoreObjective objective)
    {
        if (objective == null)
        {
            Map<ScoreObjective, Score> map = entitiesScoreObjectives.remove(name);

            if (map != null)
            {
                broadcastScoreUpdate(name);
            }
        }
        else
        {
            Map<ScoreObjective, Score> map2 = entitiesScoreObjectives.get(name);

            if (map2 != null)
            {
                Score score = map2.remove(objective);

                if (map2.size() < 1)
                {
                    Map<ScoreObjective, Score> map1 = entitiesScoreObjectives.remove(name);

                    if (map1 != null)
                    {
                        broadcastScoreUpdate(name);
                    }
                }
                else if (score != null)
                {
                    broadcastScoreUpdate(name, objective);
                }
            }
        }
    }

    public Collection<Score> getScores()
    {
        Collection<Map<ScoreObjective, Score>> collection = entitiesScoreObjectives.values();
        List<Score> list = Lists.newArrayList();

        for (Map<ScoreObjective, Score> map : collection)
        {
            list.addAll(map.values());
        }

        return list;
    }

    public Map<ScoreObjective, Score> getObjectivesForEntity(String name)
    {
        Map<ScoreObjective, Score> map = entitiesScoreObjectives.get(name);

        if (map == null)
        {
            map = Maps.newHashMap();
        }

        return map;
    }

    public void removeObjective(ScoreObjective objective)
    {
        scoreObjectives.remove(objective.getName());

        for (int i = 0; i < 19; ++i)
        {
            if (getObjectiveInDisplaySlot(i) == objective)
            {
                setObjectiveInDisplaySlot(i, null);
            }
        }

        List<ScoreObjective> list = scoreObjectiveCriterias.get(objective.getCriteria());

        if (list != null)
        {
            list.remove(objective);
        }

        for (Map<ScoreObjective, Score> map : entitiesScoreObjectives.values())
        {
            map.remove(objective);
        }

        onScoreObjectiveRemoved(objective);
    }

    /**
     * 0 is tab menu, 1 is sidebar, 2 is below name
     */
    public void setObjectiveInDisplaySlot(int objectiveSlot, ScoreObjective objective)
    {
        objectiveDisplaySlots[objectiveSlot] = objective;
    }

    @Nullable

    /**
     * 0 is tab menu, 1 is sidebar, 2 is below name
     */
    public ScoreObjective getObjectiveInDisplaySlot(int slotIn)
    {
        return objectiveDisplaySlots[slotIn];
    }

    /**
     * Retrieve the ScorePlayerTeam instance identified by the passed team name
     */
    public ScorePlayerTeam getTeam(String teamName)
    {
        return teams.get(teamName);
    }

    public ScorePlayerTeam createTeam(String name)
    {
        if (name.length() > 16)
        {
            throw new IllegalArgumentException("The team name '" + name + "' is too long!");
        }
        else
        {
            ScorePlayerTeam scoreplayerteam = getTeam(name);

            if (scoreplayerteam != null)
            {
                throw new IllegalArgumentException("A team with the name '" + name + "' already exists!");
            }
            else
            {
                scoreplayerteam = new ScorePlayerTeam(this, name);
                teams.put(name, scoreplayerteam);
                broadcastTeamCreated(scoreplayerteam);
                return scoreplayerteam;
            }
        }
    }

    /**
     * Removes the team from the scoreboard, updates all player memberships and broadcasts the deletion to all players
     */
    public void removeTeam(ScorePlayerTeam playerTeam)
    {
        teams.remove(playerTeam.getRegisteredName());

        for (String s : playerTeam.getMembershipCollection())
        {
            teamMemberships.remove(s);
        }

        broadcastTeamRemove(playerTeam);
    }

    /**
     * Adds a player to the given team
     */
    public boolean addPlayerToTeam(String player, String newTeam)
    {
        if (player.length() > 40)
        {
            throw new IllegalArgumentException("The player name '" + player + "' is too long!");
        }
        else if (!teams.containsKey(newTeam))
        {
            return false;
        }
        else
        {
            ScorePlayerTeam scoreplayerteam = getTeam(newTeam);

            if (getPlayersTeam(player) != null)
            {
                removePlayerFromTeams(player);
            }

            teamMemberships.put(player, scoreplayerteam);
            scoreplayerteam.getMembershipCollection().add(player);
            return true;
        }
    }

    public boolean removePlayerFromTeams(String playerName)
    {
        ScorePlayerTeam scoreplayerteam = getPlayersTeam(playerName);

        if (scoreplayerteam != null)
        {
            removePlayerFromTeam(playerName, scoreplayerteam);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Removes the given username from the given ScorePlayerTeam. If the player is not on the team then an
     * IllegalStateException is thrown.
     */
    public void removePlayerFromTeam(String username, ScorePlayerTeam playerTeam)
    {
        if (getPlayersTeam(username) != playerTeam)
        {
            throw new IllegalStateException("Player is either on another team or not on any team. Cannot remove from team '" + playerTeam.getRegisteredName() + "'.");
        }
        else
        {
            teamMemberships.remove(username);
            playerTeam.getMembershipCollection().remove(username);
        }
    }

    public Collection<String> getTeamNames()
    {
        return teams.keySet();
    }

    public Collection<ScorePlayerTeam> getTeams()
    {
        return teams.values();
    }

    @Nullable

    /**
     * Gets the ScorePlayerTeam object for the given username.
     */
    public ScorePlayerTeam getPlayersTeam(String username)
    {
        return teamMemberships.get(username);
    }

    /**
     * Called when a score objective is added
     */
    public void onScoreObjectiveAdded(ScoreObjective scoreObjectiveIn)
    {
    }

    public void onObjectiveDisplayNameChanged(ScoreObjective objective)
    {
    }

    public void onScoreObjectiveRemoved(ScoreObjective objective)
    {
    }

    public void onScoreUpdated(Score scoreIn)
    {
    }

    public void broadcastScoreUpdate(String scoreName)
    {
    }

    public void broadcastScoreUpdate(String scoreName, ScoreObjective objective)
    {
    }

    /**
     * This packet will notify the players that this team is created, and that will register it on the client
     */
    public void broadcastTeamCreated(ScorePlayerTeam playerTeam)
    {
    }

    /**
     * This packet will notify the players that this team is updated
     */
    public void broadcastTeamInfoUpdate(ScorePlayerTeam playerTeam)
    {
    }

    public void broadcastTeamRemove(ScorePlayerTeam playerTeam)
    {
    }

    /**
     * Returns 'list' for 0, 'sidebar' for 1, 'belowName for 2, otherwise null.
     */
    public static String getObjectiveDisplaySlot(int id)
    {
        switch (id)
        {
            case 0:
                return "list";

            case 1:
                return "sidebar";

            case 2:
                return "belowName";

            default:
                if (id >= 3 && id <= 18)
                {
                    Formatting textformatting = Formatting.fromColorIndex(id - 3);

                    if (textformatting != null && textformatting != Formatting.RESET)
                    {
                        return "sidebar.team." + textformatting.getName();
                    }
                }

                return null;
        }
    }

    /**
     * Returns 0 for (case-insensitive) 'list', 1 for 'sidebar', 2 for 'belowName', otherwise -1.
     */
    public static int getObjectiveDisplaySlotNumber(String name)
    {
        if ("list".equalsIgnoreCase(name))
        {
            return 0;
        }
        else if ("sidebar".equalsIgnoreCase(name))
        {
            return 1;
        }
        else if ("belowName".equalsIgnoreCase(name))
        {
            return 2;
        }
        else
        {
            if (name.startsWith("sidebar.team."))
            {
                String s = name.substring("sidebar.team.".length());
                Formatting textformatting = Formatting.getValueByName(s);

                if (textformatting != null && textformatting.getIndex() >= 0)
                {
                    return textformatting.getIndex() + 3;
                }
            }

            return -1;
        }
    }

    public static String[] getDisplaySlotStrings()
    {
        if (displaySlots == null)
        {
            displaySlots = new String[19];

            for (int i = 0; i < 19; ++i)
            {
                displaySlots[i] = getObjectiveDisplaySlot(i);
            }
        }

        return displaySlots;
    }

    public void removeEntity(Entity entityIn)
    {
        if (entityIn != null && !(entityIn instanceof EntityPlayer) && !entityIn.isEntityAlive())
        {
            String s = entityIn.getCachedUniqueIdString();
            removeObjectiveFromEntity(s, null);
            removePlayerFromTeams(s);
        }
    }
}
