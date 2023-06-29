package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.util.UUID;

public class UserListOpsEntry extends UserListEntry<GameProfile>
{
    private final int permissionLevel;
    private final boolean bypassesPlayerLimit;

    public UserListOpsEntry(GameProfile player, int permissionLevelIn, boolean bypassesPlayerLimitIn)
    {
        super(player);
        permissionLevel = permissionLevelIn;
        bypassesPlayerLimit = bypassesPlayerLimitIn;
    }

    public UserListOpsEntry(JsonObject p_i1150_1_)
    {
        super(constructProfile(p_i1150_1_), p_i1150_1_);
        permissionLevel = p_i1150_1_.has("level") ? p_i1150_1_.get("level").getAsInt() : 0;
        bypassesPlayerLimit = p_i1150_1_.has("bypassesPlayerLimit") && p_i1150_1_.get("bypassesPlayerLimit").getAsBoolean();
    }

    /**
     * Gets the permission level of the user, as defined in the "level" attribute of the ops.json file
     */
    public int getPermissionLevel()
    {
        return permissionLevel;
    }

    public boolean bypassesPlayerLimit()
    {
        return bypassesPlayerLimit;
    }

    protected void onSerialization(JsonObject data)
    {
        if (getValue() != null)
        {
            data.addProperty("uuid", getValue().getId() == null ? "" : getValue().getId().toString());
            data.addProperty("name", getValue().getName());
            super.onSerialization(data);
            data.addProperty("level", Integer.valueOf(permissionLevel));
            data.addProperty("bypassesPlayerLimit", Boolean.valueOf(bypassesPlayerLimit));
        }
    }

    private static GameProfile constructProfile(JsonObject p_152643_0_)
    {
        if (p_152643_0_.has("uuid") && p_152643_0_.has("name"))
        {
            String s = p_152643_0_.get("uuid").getAsString();
            UUID uuid;

            try
            {
                uuid = UUID.fromString(s);
            }
            catch (Throwable var4)
            {
                return null;
            }

            return new GameProfile(uuid, p_152643_0_.get("name").getAsString());
        }
        else
        {
            return null;
        }
    }
}
