package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BossInfo;

public class BossInfoClient extends BossInfo
{
    protected float rawPercent;
    protected long percentSetTime;

    public BossInfoClient(SPacketUpdateBossInfo packetIn)
    {
        super(packetIn.getUniqueId(), packetIn.getName(), packetIn.getColor(), packetIn.getOverlay());
        rawPercent = packetIn.getPercent();
        percent = packetIn.getPercent();
        percentSetTime = Minecraft.getSystemTime();
        setDarkenSky(packetIn.shouldDarkenSky());
        setPlayEndBossMusic(packetIn.shouldPlayEndBossMusic());
        setCreateFog(packetIn.shouldCreateFog());
    }

    public void setPercent(float percentIn)
    {
        percent = getPercent();
        rawPercent = percentIn;
        percentSetTime = Minecraft.getSystemTime();
    }

    public float getPercent()
    {
        long i = Minecraft.getSystemTime() - percentSetTime;
        float f = MathHelper.clamp((float)i / 100.0F, 0.0F, 1.0F);
        return percent + (rawPercent - percent) * f;
    }

    public void updateFromPacket(SPacketUpdateBossInfo packetIn)
    {
        switch (packetIn.getOperation())
        {
            case UPDATE_NAME:
                setName(packetIn.getName());
                break;

            case UPDATE_PCT:
                setPercent(packetIn.getPercent());
                break;

            case UPDATE_STYLE:
                setColor(packetIn.getColor());
                setOverlay(packetIn.getOverlay());
                break;

            case UPDATE_PROPERTIES:
                setDarkenSky(packetIn.shouldDarkenSky());
                setPlayEndBossMusic(packetIn.shouldPlayEndBossMusic());
        }
    }
}
