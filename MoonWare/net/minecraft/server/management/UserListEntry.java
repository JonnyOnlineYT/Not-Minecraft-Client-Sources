package net.minecraft.server.management;

import com.google.gson.JsonObject;

public class UserListEntry<T>
{
    private final T value;

    public UserListEntry(T valueIn)
    {
        value = valueIn;
    }

    protected UserListEntry(T valueIn, JsonObject json)
    {
        value = valueIn;
    }

    T getValue()
    {
        return value;
    }

    boolean hasBanExpired()
    {
        return false;
    }

    protected void onSerialization(JsonObject data)
    {
    }
}
