package net.minecraft.scoreboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketDisplayObjective;
import net.minecraft.network.play.server.SPacketScoreboardObjective;
import net.minecraft.network.play.server.SPacketTeams;
import net.minecraft.network.play.server.SPacketUpdateScore;
import net.minecraft.server.MinecraftServer;

public class ServerScoreboard extends Scoreboard
{
    private final MinecraftServer scoreboardMCServer;
    private final Set<ScoreObjective> addedObjectives = Sets.newHashSet();
    private Runnable[] dirtyRunnables = new Runnable[0];

    public ServerScoreboard(MinecraftServer mcServer)
    {
        scoreboardMCServer = mcServer;
    }

    public void onScoreUpdated(Score scoreIn)
    {
        super.onScoreUpdated(scoreIn);

        if (addedObjectives.contains(scoreIn.getObjective()))
        {
            scoreboardMCServer.getPlayerList().sendPacketToAllPlayers(new SPacketUpdateScore(scoreIn));
        }

        markSaveDataDirty();
    }

    public void broadcastScoreUpdate(String scoreName)
    {
        super.broadcastScoreUpdate(scoreName);
        scoreboardMCServer.getPlayerList().sendPacketToAllPlayers(new SPacketUpdateScore(scoreName));
        markSaveDataDirty();
    }

    public void broadcastScoreUpdate(String scoreName, ScoreObjective objective)
    {
        super.broadcastScoreUpdate(scoreName, objective);
        scoreboardMCServer.getPlayerList().sendPacketToAllPlayers(new SPacketUpdateScore(scoreName, objective));
        markSaveDataDirty();
    }

    /**
     * 0 is tab menu, 1 is sidebar, 2 is below name
     */
    public void setObjectiveInDisplaySlot(int objectiveSlot, ScoreObjective objective)
    {
        ScoreObjective scoreobjective = getObjectiveInDisplaySlot(objectiveSlot);
        super.setObjectiveInDisplaySlot(objectiveSlot, objective);

        if (scoreobjective != objective && scoreobjective != null)
        {
            if (getObjectiveDisplaySlotCount(scoreobjective) > 0)
            {
                scoreboardMCServer.getPlayerList().sendPacketToAllPlayers(new SPacketDisplayObjective(objectiveSlot, objective));
            }
            else
            {
                sendDisplaySlotRemovalPackets(scoreobjective);
            }
        }

        if (objective != null)
        {
            if (addedObjectives.contains(objective))
            {
                scoreboardMCServer.getPlayerList().sendPacketToAllPlayers(new SPacketDisplayObjective(objectiveSlot, objective));
            }
            else
            {
                addObjective(objective);
            }
        }

        markSaveDataDirty();
    }

    /**
     * Adds a player to the given team
     */
    public boolean addPlayerToTeam(String player, String newTeam)
    {
        if (super.addPlayerToTeam(player, newTeam))
        {
            ScorePlayerTeam scoreplayerteam = getTeam(newTeam);
            scoreboardMCServer.getPlayerList().sendPacketToAllPlayers(new SPacketTeams(scoreplayerteam, Arrays.asList(player), 3));
            markSaveDataDirty();
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
        super.removePlayerFromTeam(username, playerTeam);
        scoreboardMCServer.getPlayerList().sendPacketToAllPlayers(new SPacketTeams(playerTeam, Arrays.asList(username), 4));
        markSaveDataDirty();
    }

    /**
     * Called when a score objective is added
     */
    public void onScoreObjectiveAdded(ScoreObjective scoreObjectiveIn)
    {
        super.onScoreObjectiveAdded(scoreObjectiveIn);
        markSaveDataDirty();
    }

    public void onObjectiveDisplayNameChanged(ScoreObjective objective)
    {
        super.onObjectiveDisplayNameChanged(objective);

        if (addedObjectives.contains(objective))
        {
            scoreboardMCServer.getPlayerList().sendPacketToAllPlayers(new SPacketScoreboardObjective(objective, 2));
        }

        markSaveDataDirty();
    }

    public void onScoreObjectiveRemoved(ScoreObjective objective)
    {
        super.onScoreObjectiveRemoved(objective);

        if (addedObjectives.contains(objective))
        {
            sendDisplaySlotRemovalPackets(objective);
        }

        markSaveDataDirty();
    }

    /**
     * This packet will notify the players that this team is created, and that will register it on the client
     */
    public void broadcastTeamCreated(ScorePlayerTeam playerTeam)
    {
        super.broadcastTeamCreated(playerTeam);
        scoreboardMCServer.getPlayerList().sendPacketToAllPlayers(new SPacketTeams(playerTeam, 0));
        markSaveDataDirty();
    }

    /**
     * This packet will notify the players that this team is updated
     */
    public void broadcastTeamInfoUpdate(ScorePlayerTeam playerTeam)
    {
        super.broadcastTeamInfoUpdate(playerTeam);
        scoreboardMCServer.getPlayerList().sendPacketToAllPlayers(new SPacketTeams(playerTeam, 2));
        markSaveDataDirty();
    }

    public void broadcastTeamRemove(ScorePlayerTeam playerTeam)
    {
        super.broadcastTeamRemove(playerTeam);
        scoreboardMCServer.getPlayerList().sendPacketToAllPlayers(new SPacketTeams(playerTeam, 1));
        markSaveDataDirty();
    }

    public void addDirtyRunnable(Runnable runnable)
    {
        dirtyRunnables = Arrays.copyOf(dirtyRunnables, dirtyRunnables.length + 1);
        dirtyRunnables[dirtyRunnables.length - 1] = runnable;
    }

    protected void markSaveDataDirty()
    {
        for (Runnable runnable : dirtyRunnables)
        {
            runnable.run();
        }
    }

    public List < Packet<? >> getCreatePackets(ScoreObjective objective)
    {
        List < Packet<? >> list = Lists.newArrayList();
        list.add(new SPacketScoreboardObjective(objective, 0));

        for (int i = 0; i < 19; ++i)
        {
            if (getObjectiveInDisplaySlot(i) == objective)
            {
                list.add(new SPacketDisplayObjective(i, objective));
            }
        }

        for (Score score : getSortedScores(objective))
        {
            list.add(new SPacketUpdateScore(score));
        }

        return list;
    }

    public void addObjective(ScoreObjective objective)
    {
        List < Packet<? >> list = getCreatePackets(objective);

        for (EntityPlayerMP entityplayermp : scoreboardMCServer.getPlayerList().getPlayerList())
        {
            for (Packet<?> packet : list)
            {
                entityplayermp.connection.sendPacket(packet);
            }
        }

        addedObjectives.add(objective);
    }

    public List < Packet<? >> getDestroyPackets(ScoreObjective p_96548_1_)
    {
        List < Packet<? >> list = Lists.newArrayList();
        list.add(new SPacketScoreboardObjective(p_96548_1_, 1));

        for (int i = 0; i < 19; ++i)
        {
            if (getObjectiveInDisplaySlot(i) == p_96548_1_)
            {
                list.add(new SPacketDisplayObjective(i, p_96548_1_));
            }
        }

        return list;
    }

    public void sendDisplaySlotRemovalPackets(ScoreObjective p_96546_1_)
    {
        List < Packet<? >> list = getDestroyPackets(p_96546_1_);

        for (EntityPlayerMP entityplayermp : scoreboardMCServer.getPlayerList().getPlayerList())
        {
            for (Packet<?> packet : list)
            {
                entityplayermp.connection.sendPacket(packet);
            }
        }

        addedObjectives.remove(p_96546_1_);
    }

    public int getObjectiveDisplaySlotCount(ScoreObjective p_96552_1_)
    {
        int i = 0;

        for (int j = 0; j < 19; ++j)
        {
            if (getObjectiveInDisplaySlot(j) == p_96552_1_)
            {
                ++i;
            }
        }

        return i;
    }
}
