package net.minecraft.network.play.server;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.io.IOException;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.Component;
import net.minecraft.world.GameType;

public class SPacketPlayerListItem implements Packet<INetHandlerPlayClient>
{
    private SPacketPlayerListItem.Action action;
    private final List<SPacketPlayerListItem.AddPlayerData> players = Lists.newArrayList();

    public SPacketPlayerListItem()
    {
    }

    public SPacketPlayerListItem(SPacketPlayerListItem.Action actionIn, EntityPlayerMP... playersIn)
    {
        action = actionIn;

        for (EntityPlayerMP entityplayermp : playersIn)
        {
            players.add(new SPacketPlayerListItem.AddPlayerData(entityplayermp.getGameProfile(), entityplayermp.ping, entityplayermp.interactionManager.getGameType(), entityplayermp.getTabListDisplayName()));
        }
    }

    public SPacketPlayerListItem(SPacketPlayerListItem.Action actionIn, Iterable<EntityPlayerMP> playersIn)
    {
        action = actionIn;

        for (EntityPlayerMP entityplayermp : playersIn)
        {
            players.add(new SPacketPlayerListItem.AddPlayerData(entityplayermp.getGameProfile(), entityplayermp.ping, entityplayermp.interactionManager.getGameType(), entityplayermp.getTabListDisplayName()));
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        action = buf.readEnumValue(Action.class);
        int i = buf.readVarIntFromBuffer();

        for (int j = 0; j < i; ++j)
        {
            GameProfile gameprofile = null;
            int k = 0;
            GameType gametype = null;
            Component itextcomponent = null;

            switch (action)
            {
                case ADD_PLAYER:
                    gameprofile = new GameProfile(buf.readUuid(), buf.readStringFromBuffer(16));
                    int l = buf.readVarIntFromBuffer();
                    int i1 = 0;

                    for (; i1 < l; ++i1)
                    {
                        String s = buf.readStringFromBuffer(32767);
                        String s1 = buf.readStringFromBuffer(32767);

                        if (buf.readBoolean())
                        {
                            gameprofile.getProperties().put(s, new Property(s, s1, buf.readStringFromBuffer(32767)));
                        }
                        else
                        {
                            gameprofile.getProperties().put(s, new Property(s, s1));
                        }
                    }

                    gametype = GameType.getByID(buf.readVarIntFromBuffer());
                    k = buf.readVarIntFromBuffer();

                    if (buf.readBoolean())
                    {
                        itextcomponent = buf.readTextComponent();
                    }

                    break;

                case UPDATE_GAME_MODE:
                    gameprofile = new GameProfile(buf.readUuid(), null);
                    gametype = GameType.getByID(buf.readVarIntFromBuffer());
                    break;

                case UPDATE_LATENCY:
                    gameprofile = new GameProfile(buf.readUuid(), null);
                    k = buf.readVarIntFromBuffer();
                    break;

                case UPDATE_DISPLAY_NAME:
                    gameprofile = new GameProfile(buf.readUuid(), null);

                    if (buf.readBoolean())
                    {
                        itextcomponent = buf.readTextComponent();
                    }

                    break;

                case REMOVE_PLAYER:
                    gameprofile = new GameProfile(buf.readUuid(), null);
            }

            players.add(new SPacketPlayerListItem.AddPlayerData(gameprofile, k, gametype, itextcomponent));
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeEnumValue(action);
        buf.writeVarIntToBuffer(players.size());

        for (SPacketPlayerListItem.AddPlayerData spacketplayerlistitem$addplayerdata : players)
        {
            switch (action)
            {
                case ADD_PLAYER:
                    buf.writeUuid(spacketplayerlistitem$addplayerdata.getProfile().getId());
                    buf.writeString(spacketplayerlistitem$addplayerdata.getProfile().getName());
                    buf.writeVarIntToBuffer(spacketplayerlistitem$addplayerdata.getProfile().getProperties().size());

                    for (Property property : spacketplayerlistitem$addplayerdata.getProfile().getProperties().values())
                    {
                        buf.writeString(property.getName());
                        buf.writeString(property.getValue());

                        if (property.hasSignature())
                        {
                            buf.writeBoolean(true);
                            buf.writeString(property.getSignature());
                        }
                        else
                        {
                            buf.writeBoolean(false);
                        }
                    }

                    buf.writeVarIntToBuffer(spacketplayerlistitem$addplayerdata.getGameMode().getID());
                    buf.writeVarIntToBuffer(spacketplayerlistitem$addplayerdata.getPing());

                    if (spacketplayerlistitem$addplayerdata.getDisplayName() == null)
                    {
                        buf.writeBoolean(false);
                    }
                    else
                    {
                        buf.writeBoolean(true);
                        buf.writeTextComponent(spacketplayerlistitem$addplayerdata.getDisplayName());
                    }

                    break;

                case UPDATE_GAME_MODE:
                    buf.writeUuid(spacketplayerlistitem$addplayerdata.getProfile().getId());
                    buf.writeVarIntToBuffer(spacketplayerlistitem$addplayerdata.getGameMode().getID());
                    break;

                case UPDATE_LATENCY:
                    buf.writeUuid(spacketplayerlistitem$addplayerdata.getProfile().getId());
                    buf.writeVarIntToBuffer(spacketplayerlistitem$addplayerdata.getPing());
                    break;

                case UPDATE_DISPLAY_NAME:
                    buf.writeUuid(spacketplayerlistitem$addplayerdata.getProfile().getId());

                    if (spacketplayerlistitem$addplayerdata.getDisplayName() == null)
                    {
                        buf.writeBoolean(false);
                    }
                    else
                    {
                        buf.writeBoolean(true);
                        buf.writeTextComponent(spacketplayerlistitem$addplayerdata.getDisplayName());
                    }

                    break;

                case REMOVE_PLAYER:
                    buf.writeUuid(spacketplayerlistitem$addplayerdata.getProfile().getId());
            }
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handlePlayerListItem(this);
    }

    public List<SPacketPlayerListItem.AddPlayerData> getEntries()
    {
        return players;
    }

    public SPacketPlayerListItem.Action getAction()
    {
        return action;
    }

    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("action", action).add("entries", players).toString();
    }

    public enum Action
    {
        ADD_PLAYER,
        UPDATE_GAME_MODE,
        UPDATE_LATENCY,
        UPDATE_DISPLAY_NAME,
        REMOVE_PLAYER
    }

    public class AddPlayerData
    {
        private final int ping;
        private final GameType gamemode;
        private final GameProfile profile;
        private final Component displayName;

        public AddPlayerData(GameProfile profileIn, int latencyIn, GameType gameModeIn, @Nullable Component displayNameIn)
        {
            profile = profileIn;
            ping = latencyIn;
            gamemode = gameModeIn;
            displayName = displayNameIn;
        }

        public GameProfile getProfile()
        {
            return profile;
        }

        public int getPing()
        {
            return ping;
        }

        public GameType getGameMode()
        {
            return gamemode;
        }

        @Nullable
        public Component getDisplayName()
        {
            return displayName;
        }

        public String toString()
        {
            return MoreObjects.toStringHelper(this).add("latency", ping).add("gameMode", gamemode).add("profile", profile).add("displayName", displayName == null ? null : Component.Serializer.componentToJson(displayName)).toString();
        }
    }
}
