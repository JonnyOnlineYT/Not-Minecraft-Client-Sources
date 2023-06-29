package optifine;

import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

public class WorldServerOF extends WorldServer
{
    private MinecraftServer mcServer;

    public WorldServerOF(MinecraftServer p_i103_1_, ISaveHandler p_i103_2_, WorldInfo p_i103_3_, int p_i103_4_, Profiler p_i103_5_)
    {
        super(p_i103_1_, p_i103_2_, p_i103_3_, p_i103_4_, p_i103_5_);
        mcServer = p_i103_1_;
    }

    /**
     * Runs a single tick for the world
     */
    public void tick()
    {
        super.tick();

        if (!Config.isTimeDefault())
        {
            fixWorldTime();
        }

        if (Config.waterOpacityChanged)
        {
            Config.waterOpacityChanged = false;
            ClearWater.updateWaterOpacity(Config.getGameSettings(), this);
        }
    }

    /**
     * Updates all weather states.
     */
    protected void updateWeather()
    {
        if (!Config.isWeatherEnabled())
        {
            fixWorldWeather();
        }

        super.updateWeather();
    }

    private void fixWorldWeather()
    {
        if (worldInfo.isRaining() || worldInfo.isThundering())
        {
            worldInfo.setRainTime(0);
            worldInfo.setRaining(false);
            setRainStrength(0.0F);
            worldInfo.setThunderTime(0);
            worldInfo.setThundering(false);
            setThunderStrength(0.0F);
            mcServer.getPlayerList().sendPacketToAllPlayers(new SPacketChangeGameState(2, 0.0F));
            mcServer.getPlayerList().sendPacketToAllPlayers(new SPacketChangeGameState(7, 0.0F));
            mcServer.getPlayerList().sendPacketToAllPlayers(new SPacketChangeGameState(8, 0.0F));
        }
    }

    private void fixWorldTime()
    {
        if (worldInfo.getGameType().getID() == 1)
        {
            long i = getWorldTime();
            long j = i % 24000L;

            if (Config.isTimeDayOnly())
            {
                if (j <= 1000L)
                {
                    setWorldTime(i - j + 1001L);
                }

                if (j >= 11000L)
                {
                    setWorldTime(i - j + 24001L);
                }
            }

            if (Config.isTimeNightOnly())
            {
                if (j <= 14000L)
                {
                    setWorldTime(i - j + 14001L);
                }

                if (j >= 22000L)
                {
                    setWorldTime(i - j + 24000L + 14001L);
                }
            }
        }
    }
}
