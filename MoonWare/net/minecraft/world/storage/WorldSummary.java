package net.minecraft.world.storage;

import net.minecraft.util.StringUtils;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.GameType;

public class WorldSummary implements Comparable<WorldSummary>
{
    /** the file name of this save */
    private final String fileName;

    /** the displayed name of this save file */
    private final String displayName;
    private final long lastTimePlayed;
    private final long sizeOnDisk;
    private final boolean requiresConversion;

    /** Instance of EnumGameType. */
    private final GameType theEnumGameType;
    private final boolean hardcore;
    private final boolean cheatsEnabled;
    private final String versionName;
    private final int versionId;
    private final boolean versionSnapshot;

    public WorldSummary(WorldInfo info, String fileNameIn, String displayNameIn, long sizeOnDiskIn, boolean requiresConversionIn)
    {
        fileName = fileNameIn;
        displayName = displayNameIn;
        lastTimePlayed = info.getLastTimePlayed();
        sizeOnDisk = sizeOnDiskIn;
        theEnumGameType = info.getGameType();
        requiresConversion = requiresConversionIn;
        hardcore = info.isHardcoreModeEnabled();
        cheatsEnabled = info.areCommandsAllowed();
        versionName = info.getVersionName();
        versionId = info.getVersionId();
        versionSnapshot = info.isVersionSnapshot();
    }

    /**
     * return the file name
     */
    public String getFileName()
    {
        return fileName;
    }

    /**
     * return the display name of the save
     */
    public String getDisplayName()
    {
        return displayName;
    }

    public long getSizeOnDisk()
    {
        return sizeOnDisk;
    }

    public boolean requiresConversion()
    {
        return requiresConversion;
    }

    public long getLastTimePlayed()
    {
        return lastTimePlayed;
    }

    public int compareTo(WorldSummary p_compareTo_1_)
    {
        if (lastTimePlayed < p_compareTo_1_.lastTimePlayed)
        {
            return 1;
        }
        else
        {
            return lastTimePlayed > p_compareTo_1_.lastTimePlayed ? -1 : fileName.compareTo(p_compareTo_1_.fileName);
        }
    }

    /**
     * Gets the EnumGameType.
     */
    public GameType getEnumGameType()
    {
        return theEnumGameType;
    }

    public boolean isHardcoreModeEnabled()
    {
        return hardcore;
    }

    /**
     * @return {@code true} if cheats are enabled for this world
     */
    public boolean getCheatsEnabled()
    {
        return cheatsEnabled;
    }

    public String getVersionName()
    {
        return StringUtils.isNullOrEmpty(versionName) ? I18n.translateToLocal("selectWorld.versionUnknown") : versionName;
    }

    public boolean markVersionInList()
    {
        return askToOpenWorld();
    }

    public boolean askToOpenWorld()
    {
        return versionId > 1343;
    }
}
