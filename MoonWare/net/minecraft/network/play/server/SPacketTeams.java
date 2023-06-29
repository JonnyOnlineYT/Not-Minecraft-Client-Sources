package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Collection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;

public class SPacketTeams implements Packet<INetHandlerPlayClient>
{
    private String name = "";
    private String displayName = "";
    private String prefix = "";
    private String suffix = "";
    private String nameTagVisibility;
    private String collisionRule;
    private int color;
    private final Collection<String> players;
    private int action;
    private int friendlyFlags;

    public SPacketTeams()
    {
        nameTagVisibility = Team.EnumVisible.ALWAYS.internalName;
        collisionRule = Team.CollisionRule.ALWAYS.name;
        color = -1;
        players = Lists.newArrayList();
    }

    public SPacketTeams(ScorePlayerTeam teamIn, int actionIn)
    {
        nameTagVisibility = Team.EnumVisible.ALWAYS.internalName;
        collisionRule = Team.CollisionRule.ALWAYS.name;
        color = -1;
        players = Lists.newArrayList();
        name = teamIn.getRegisteredName();
        action = actionIn;

        if (actionIn == 0 || actionIn == 2)
        {
            displayName = teamIn.getTeamName();
            prefix = teamIn.getColorPrefix();
            suffix = teamIn.getColorSuffix();
            friendlyFlags = teamIn.getFriendlyFlags();
            nameTagVisibility = teamIn.getNameTagVisibility().internalName;
            collisionRule = teamIn.getCollisionRule().name;
            color = teamIn.getChatFormat().getIndex();
        }

        if (actionIn == 0)
        {
            players.addAll(teamIn.getMembershipCollection());
        }
    }

    public SPacketTeams(ScorePlayerTeam teamIn, Collection<String> playersIn, int actionIn)
    {
        nameTagVisibility = Team.EnumVisible.ALWAYS.internalName;
        collisionRule = Team.CollisionRule.ALWAYS.name;
        color = -1;
        players = Lists.newArrayList();

        if (actionIn != 3 && actionIn != 4)
        {
            throw new IllegalArgumentException("Method must be join or leave for player constructor");
        }
        else if (playersIn != null && !playersIn.isEmpty())
        {
            action = actionIn;
            name = teamIn.getRegisteredName();
            players.addAll(playersIn);
        }
        else
        {
            throw new IllegalArgumentException("Players cannot be null/empty");
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        name = buf.readStringFromBuffer(16);
        action = buf.readByte();

        if (action == 0 || action == 2)
        {
            displayName = buf.readStringFromBuffer(32);
            prefix = buf.readStringFromBuffer(16);
            suffix = buf.readStringFromBuffer(16);
            friendlyFlags = buf.readByte();
            nameTagVisibility = buf.readStringFromBuffer(32);
            collisionRule = buf.readStringFromBuffer(32);
            color = buf.readByte();
        }

        if (action == 0 || action == 3 || action == 4)
        {
            int i = buf.readVarIntFromBuffer();

            for (int j = 0; j < i; ++j)
            {
                players.add(buf.readStringFromBuffer(40));
            }
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeString(name);
        buf.writeByte(action);

        if (action == 0 || action == 2)
        {
            buf.writeString(displayName);
            buf.writeString(prefix);
            buf.writeString(suffix);
            buf.writeByte(friendlyFlags);
            buf.writeString(nameTagVisibility);
            buf.writeString(collisionRule);
            buf.writeByte(color);
        }

        if (action == 0 || action == 3 || action == 4)
        {
            buf.writeVarIntToBuffer(players.size());

            for (String s : players)
            {
                buf.writeString(s);
            }
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleTeams(this);
    }

    public String getName()
    {
        return name;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public String getSuffix()
    {
        return suffix;
    }

    public Collection<String> getPlayers()
    {
        return players;
    }

    public int getAction()
    {
        return action;
    }

    public int getFriendlyFlags()
    {
        return friendlyFlags;
    }

    public int getColor()
    {
        return color;
    }

    public String getNameTagVisibility()
    {
        return nameTagVisibility;
    }

    public String getCollisionRule()
    {
        return collisionRule;
    }
}
