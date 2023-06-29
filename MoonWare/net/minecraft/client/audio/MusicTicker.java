package net.minecraft.client.audio;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;

public class MusicTicker implements ITickable
{
    private final Random rand = new Random();
    private final Minecraft mc;
    private ISound currentMusic;
    private int timeUntilNextMusic = 100;

    public MusicTicker(Minecraft mcIn)
    {
        mc = mcIn;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    public void update()
    {
        MusicTicker.MusicType musicticker$musictype = mc.getAmbientMusicType();

        if (currentMusic != null)
        {
            if (!musicticker$musictype.getMusicLocation().getSoundName().equals(currentMusic.getSoundLocation()))
            {
                Minecraft.getSoundHandler().stopSound(currentMusic);
                timeUntilNextMusic = MathHelper.getInt(rand, 0, musicticker$musictype.getMinDelay() / 2);
            }

            if (!Minecraft.getSoundHandler().isSoundPlaying(currentMusic))
            {
                currentMusic = null;
                timeUntilNextMusic = Math.min(MathHelper.getInt(rand, musicticker$musictype.getMinDelay(), musicticker$musictype.getMaxDelay()), timeUntilNextMusic);
            }
        }

        timeUntilNextMusic = Math.min(timeUntilNextMusic, musicticker$musictype.getMaxDelay());

        if (currentMusic == null && timeUntilNextMusic-- <= 0)
        {
            playMusic(musicticker$musictype);
        }
    }

    /**
     * Plays a music track for the maximum allowable period of time
     */
    public void playMusic(MusicTicker.MusicType requestedMusicType)
    {
        currentMusic = PositionedSoundRecord.getMusicRecord(requestedMusicType.getMusicLocation());
        Minecraft.getSoundHandler().playSound(currentMusic);
        timeUntilNextMusic = Integer.MAX_VALUE;
    }

    public enum MusicType
    {
        MENU(SoundEvents.MUSIC_MENU, 20, 600),
        GAME(SoundEvents.MUSIC_GAME, 12000, 24000),
        CREATIVE(SoundEvents.MUSIC_CREATIVE, 1200, 3600),
        CREDITS(SoundEvents.MUSIC_CREDITS, 0, 0),
        NETHER(SoundEvents.MUSIC_NETHER, 1200, 3600),
        END_BOSS(SoundEvents.MUSIC_DRAGON, 0, 0),
        END(SoundEvents.MUSIC_END, 6000, 24000);

        private final SoundEvent musicLocation;
        private final int minDelay;
        private final int maxDelay;

        MusicType(SoundEvent musicLocationIn, int minDelayIn, int maxDelayIn)
        {
            musicLocation = musicLocationIn;
            minDelay = minDelayIn;
            maxDelay = maxDelayIn;
        }

        public SoundEvent getMusicLocation()
        {
            return musicLocation;
        }

        public int getMinDelay()
        {
            return minDelay;
        }

        public int getMaxDelay()
        {
            return maxDelay;
        }
    }
}
