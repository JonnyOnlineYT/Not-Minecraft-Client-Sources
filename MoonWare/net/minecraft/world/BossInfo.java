package net.minecraft.world;

import java.util.UUID;
import net.minecraft.util.text.Component;

public abstract class BossInfo
{
    private final UUID uniqueId;
    protected Component name;
    protected float percent;
    protected BossInfo.Color color;
    protected BossInfo.Overlay overlay;
    protected boolean darkenSky;
    protected boolean playEndBossMusic;
    protected boolean createFog;

    public BossInfo(UUID uniqueIdIn, Component nameIn, BossInfo.Color colorIn, BossInfo.Overlay overlayIn)
    {
        uniqueId = uniqueIdIn;
        name = nameIn;
        color = colorIn;
        overlay = overlayIn;
        percent = 1.0F;
    }

    public UUID getUniqueId()
    {
        return uniqueId;
    }

    public Component getName()
    {
        return name;
    }

    public void setName(Component nameIn)
    {
        name = nameIn;
    }

    public float getPercent()
    {
        return percent;
    }

    public void setPercent(float percentIn)
    {
        percent = percentIn;
    }

    public BossInfo.Color getColor()
    {
        return color;
    }

    public void setColor(BossInfo.Color colorIn)
    {
        color = colorIn;
    }

    public BossInfo.Overlay getOverlay()
    {
        return overlay;
    }

    public void setOverlay(BossInfo.Overlay overlayIn)
    {
        overlay = overlayIn;
    }

    public boolean shouldDarkenSky()
    {
        return darkenSky;
    }

    public BossInfo setDarkenSky(boolean darkenSkyIn)
    {
        darkenSky = darkenSkyIn;
        return this;
    }

    public boolean shouldPlayEndBossMusic()
    {
        return playEndBossMusic;
    }

    public BossInfo setPlayEndBossMusic(boolean playEndBossMusicIn)
    {
        playEndBossMusic = playEndBossMusicIn;
        return this;
    }

    public BossInfo setCreateFog(boolean createFogIn)
    {
        createFog = createFogIn;
        return this;
    }

    public boolean shouldCreateFog()
    {
        return createFog;
    }

    public enum Color
    {
        PINK,
        BLUE,
        RED,
        GREEN,
        YELLOW,
        PURPLE,
        WHITE
    }

    public enum Overlay
    {
        PROGRESS,
        NOTCHED_6,
        NOTCHED_10,
        NOTCHED_12,
        NOTCHED_20
    }
}
