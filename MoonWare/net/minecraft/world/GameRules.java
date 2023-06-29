package net.minecraft.world;

import java.util.Set;
import java.util.TreeMap;
import net.minecraft.nbt.NBTTagCompound;

public class GameRules
{
    private final TreeMap<String, GameRules.Value> theGameRules = new TreeMap<String, GameRules.Value>();

    public GameRules()
    {
        addGameRule("doFireTick", "true", GameRules.ValueType.BOOLEAN_VALUE);
        addGameRule("mobGriefing", "true", GameRules.ValueType.BOOLEAN_VALUE);
        addGameRule("keepInventory", "false", GameRules.ValueType.BOOLEAN_VALUE);
        addGameRule("doMobSpawning", "true", GameRules.ValueType.BOOLEAN_VALUE);
        addGameRule("doMobLoot", "true", GameRules.ValueType.BOOLEAN_VALUE);
        addGameRule("doTileDrops", "true", GameRules.ValueType.BOOLEAN_VALUE);
        addGameRule("doEntityDrops", "true", GameRules.ValueType.BOOLEAN_VALUE);
        addGameRule("commandBlockOutput", "true", GameRules.ValueType.BOOLEAN_VALUE);
        addGameRule("naturalRegeneration", "true", GameRules.ValueType.BOOLEAN_VALUE);
        addGameRule("doDaylightCycle", "true", GameRules.ValueType.BOOLEAN_VALUE);
        addGameRule("logAdminCommands", "true", GameRules.ValueType.BOOLEAN_VALUE);
        addGameRule("showDeathMessages", "true", GameRules.ValueType.BOOLEAN_VALUE);
        addGameRule("randomTickSpeed", "3", GameRules.ValueType.NUMERICAL_VALUE);
        addGameRule("sendCommandFeedback", "true", GameRules.ValueType.BOOLEAN_VALUE);
        addGameRule("reducedDebugInfo", "false", GameRules.ValueType.BOOLEAN_VALUE);
        addGameRule("spectatorsGenerateChunks", "true", GameRules.ValueType.BOOLEAN_VALUE);
        addGameRule("spawnRadius", "10", GameRules.ValueType.NUMERICAL_VALUE);
        addGameRule("disableElytraMovementCheck", "false", GameRules.ValueType.BOOLEAN_VALUE);
        addGameRule("maxEntityCramming", "24", GameRules.ValueType.NUMERICAL_VALUE);
        addGameRule("doWeatherCycle", "true", GameRules.ValueType.BOOLEAN_VALUE);
        addGameRule("doLimitedCrafting", "false", GameRules.ValueType.BOOLEAN_VALUE);
        addGameRule("maxCommandChainLength", "65536", GameRules.ValueType.NUMERICAL_VALUE);
        addGameRule("announceAdvancements", "true", GameRules.ValueType.BOOLEAN_VALUE);
        addGameRule("gameLoopFunction", "-", GameRules.ValueType.FUNCTION);
    }

    public void addGameRule(String key, String value, GameRules.ValueType type)
    {
        theGameRules.put(key, new GameRules.Value(value, type));
    }

    public void setOrCreateGameRule(String key, String ruleValue)
    {
        GameRules.Value gamerules$value = theGameRules.get(key);

        if (gamerules$value != null)
        {
            gamerules$value.setValue(ruleValue);
        }
        else
        {
            addGameRule(key, ruleValue, GameRules.ValueType.ANY_VALUE);
        }
    }

    /**
     * Gets the string Game Rule value.
     */
    public String getString(String name)
    {
        GameRules.Value gamerules$value = theGameRules.get(name);
        return gamerules$value != null ? gamerules$value.getString() : "";
    }

    /**
     * Gets the boolean Game Rule value.
     */
    public boolean getBoolean(String name)
    {
        GameRules.Value gamerules$value = theGameRules.get(name);
        return gamerules$value != null && gamerules$value.getBoolean();
    }

    public int getInt(String name)
    {
        GameRules.Value gamerules$value = theGameRules.get(name);
        return gamerules$value != null ? gamerules$value.getInt() : 0;
    }

    /**
     * Return the defined game rules as NBT.
     */
    public NBTTagCompound writeToNBT()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        for (String s : theGameRules.keySet())
        {
            GameRules.Value gamerules$value = theGameRules.get(s);
            nbttagcompound.setString(s, gamerules$value.getString());
        }

        return nbttagcompound;
    }

    /**
     * Set defined game rules from NBT.
     */
    public void readFromNBT(NBTTagCompound nbt)
    {
        for (String s : nbt.getKeySet())
        {
            setOrCreateGameRule(s, nbt.getString(s));
        }
    }

    /**
     * Return the defined game rules.
     */
    public String[] getRules()
    {
        Set<String> set = theGameRules.keySet();
        return set.toArray(new String[set.size()]);
    }

    /**
     * Return whether the specified game rule is defined.
     */
    public boolean hasRule(String name)
    {
        return theGameRules.containsKey(name);
    }

    public boolean areSameType(String key, GameRules.ValueType otherValue)
    {
        GameRules.Value gamerules$value = theGameRules.get(key);
        return gamerules$value != null && (gamerules$value.getType() == otherValue || otherValue == GameRules.ValueType.ANY_VALUE);
    }

    static class Value
    {
        private String valueString;
        private boolean valueBoolean;
        private int valueInteger;
        private double valueDouble;
        private final GameRules.ValueType type;

        public Value(String value, GameRules.ValueType type)
        {
            this.type = type;
            setValue(value);
        }

        public void setValue(String value)
        {
            valueString = value;

            if (value != null)
            {
                if (value.equals("false"))
                {
                    valueBoolean = false;
                    return;
                }

                if (value.equals("true"))
                {
                    valueBoolean = true;
                    return;
                }
            }

            valueBoolean = Boolean.parseBoolean(value);
            valueInteger = valueBoolean ? 1 : 0;

            try
            {
                valueInteger = Integer.parseInt(value);
            }
            catch (NumberFormatException var4)
            {
            }

            try
            {
                valueDouble = Double.parseDouble(value);
            }
            catch (NumberFormatException var3)
            {
            }
        }

        public String getString()
        {
            return valueString;
        }

        public boolean getBoolean()
        {
            return valueBoolean;
        }

        public int getInt()
        {
            return valueInteger;
        }

        public GameRules.ValueType getType()
        {
            return type;
        }
    }

    public enum ValueType
    {
        ANY_VALUE,
        BOOLEAN_VALUE,
        NUMERICAL_VALUE,
        FUNCTION
    }
}
