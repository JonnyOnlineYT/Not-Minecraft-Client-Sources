package net.minecraft.util;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.util.UUIDTypeAdapter;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class Session
{
    private final String username;
    private final String playerID;
    private final String token;
    private final Session.Type sessionType;

    public Session(String usernameIn, String playerIDIn, String tokenIn, String sessionTypeIn)
    {
        username = usernameIn;
        playerID = playerIDIn;
        token = tokenIn;
        sessionType = Session.Type.setSessionType(sessionTypeIn);
    }

    public String getSessionID()
    {
        return "token:" + token + ":" + playerID;
    }

    public String getPlayerID()
    {
        return playerID;
    }

    public String getUsername()
    {
        return username;
    }

    public String getToken()
    {
        return token;
    }

    public GameProfile getProfile()
    {
        try
        {
            UUID uuid = UUIDTypeAdapter.fromString(getPlayerID());
            return new GameProfile(uuid, getUsername());
        }
        catch (IllegalArgumentException var2)
        {
            return new GameProfile(null, getUsername());
        }
    }

    public enum Type
    {
        LEGACY("legacy"),
        MOJANG("mojang");

        private static final Map<String, Session.Type> SESSION_TYPES = Maps.newHashMap();
        private final String sessionType;

        Type(String sessionTypeIn)
        {
            sessionType = sessionTypeIn;
        }

        @Nullable
        public static Session.Type setSessionType(String sessionTypeIn)
        {
            return SESSION_TYPES.get(sessionTypeIn.toLowerCase(Locale.ROOT));
        }

        static {
            for (Session.Type session$type : values())
            {
                SESSION_TYPES.put(session$type.sessionType, session$type);
            }
        }
    }
}
