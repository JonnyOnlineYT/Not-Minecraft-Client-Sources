package net.minecraft.world;

public enum EnumSkyBlock
{
    SKY(15),
    BLOCK(0);

    public final int defaultLightValue;

    EnumSkyBlock(int defaultLightValueIn)
    {
        defaultLightValue = defaultLightValueIn;
    }
}
