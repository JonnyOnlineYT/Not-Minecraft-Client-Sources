package net.minecraft.world;

public enum EnumDifficulty
{
    PEACEFUL(0, "options.difficulty.peaceful"),
    EASY(1, "options.difficulty.easy"),
    NORMAL(2, "options.difficulty.normal"),
    HARD(3, "options.difficulty.hard");

    private static final EnumDifficulty[] ID_MAPPING = new EnumDifficulty[values().length];
    private final int difficultyId;
    private final String difficultyResourceKey;

    EnumDifficulty(int difficultyIdIn, String difficultyResourceKeyIn)
    {
        difficultyId = difficultyIdIn;
        difficultyResourceKey = difficultyResourceKeyIn;
    }

    public int getDifficultyId()
    {
        return difficultyId;
    }

    public static EnumDifficulty getDifficultyEnum(int p_151523_0_)
    {
        return ID_MAPPING[p_151523_0_ % ID_MAPPING.length];
    }

    public String getDifficultyResourceKey()
    {
        return difficultyResourceKey;
    }

    static {
        for (EnumDifficulty enumdifficulty : values())
        {
            ID_MAPPING[enumdifficulty.difficultyId] = enumdifficulty;
        }
    }
}
