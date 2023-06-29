package net.minecraft.world;

import net.minecraft.world.storage.WorldInfo;

public final class WorldSettings
{
    /** The seed for the map. */
    private final long seed;

    /** The EnumGameType. */
    private final GameType theGameType;

    /**
     * Switch for the map features. 'true' for enabled, 'false' for disabled.
     */
    private final boolean mapFeaturesEnabled;

    /** True if hardcore mode is enabled */
    private final boolean hardcoreEnabled;
    private final WorldType terrainType;

    /** True if Commands (cheats) are allowed. */
    private boolean commandsAllowed;

    /** True if the Bonus Chest is enabled. */
    private boolean bonusChestEnabled;
    private String generatorOptions;

    public WorldSettings(long seedIn, GameType gameType, boolean enableMapFeatures, boolean hardcoreMode, WorldType worldTypeIn)
    {
        generatorOptions = "";
        seed = seedIn;
        theGameType = gameType;
        mapFeaturesEnabled = enableMapFeatures;
        hardcoreEnabled = hardcoreMode;
        terrainType = worldTypeIn;
    }

    public WorldSettings(WorldInfo info)
    {
        this(info.getSeed(), info.getGameType(), info.isMapFeaturesEnabled(), info.isHardcoreModeEnabled(), info.getTerrainType());
    }

    /**
     * Enables the bonus chest.
     */
    public WorldSettings enableBonusChest()
    {
        bonusChestEnabled = true;
        return this;
    }

    /**
     * Enables Commands (cheats).
     */
    public WorldSettings enableCommands()
    {
        commandsAllowed = true;
        return this;
    }

    public WorldSettings setGeneratorOptions(String options)
    {
        generatorOptions = options;
        return this;
    }

    /**
     * Returns true if the Bonus Chest is enabled.
     */
    public boolean isBonusChestEnabled()
    {
        return bonusChestEnabled;
    }

    /**
     * Returns the seed for the world.
     */
    public long getSeed()
    {
        return seed;
    }

    /**
     * Gets the game type.
     */
    public GameType getGameType()
    {
        return theGameType;
    }

    /**
     * Returns true if hardcore mode is enabled, otherwise false
     */
    public boolean getHardcoreEnabled()
    {
        return hardcoreEnabled;
    }

    /**
     * Get whether the map features (e.g. strongholds) generation is enabled or disabled.
     */
    public boolean isMapFeaturesEnabled()
    {
        return mapFeaturesEnabled;
    }

    public WorldType getTerrainType()
    {
        return terrainType;
    }

    /**
     * Returns true if Commands (cheats) are allowed.
     */
    public boolean areCommandsAllowed()
    {
        return commandsAllowed;
    }

    /**
     * Gets the GameType by ID
     */
    public static GameType getGameTypeById(int id)
    {
        return GameType.getByID(id);
    }

    public String getGeneratorOptions()
    {
        return generatorOptions;
    }
}
