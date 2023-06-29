package net.minecraft.client.audio;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import io.netty.util.internal.ThreadLocalRandom;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Namespaced;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.SoundSystemLogger;
import paulscode.sound.Source;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

public class SoundManager
{
    /** The marker used for logging */
    private static final Marker LOG_MARKER = MarkerManager.getMarker("SOUNDS");
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Set<Namespaced> UNABLE_TO_PLAY = Sets.newHashSet();

    /** A reference to the sound handler. */
    private final SoundHandler sndHandler;

    /** Reference to the GameSettings object. */
    private final GameSettings options;

    /** A reference to the sound system. */
    private SoundManager.SoundSystemStarterThread sndSystem;

    /** Set to true when the SoundManager has been initialised. */
    private boolean loaded;

    /** A counter for how long the sound manager has been running */
    private int playTime;
    private final Map<String, ISound> playingSounds = HashBiMap.create();
    private final Map<ISound, String> invPlayingSounds;
    private final Multimap<SoundCategory, String> categorySounds;
    private final List<ITickableSound> tickableSounds;
    private final Map<ISound, Integer> delayedSounds;
    private final Map<String, Integer> playingSoundsStopTime;
    private final List<ISoundEventListener> listeners;
    private final List<String> pausedChannels;

    public SoundManager(SoundHandler p_i45119_1_, GameSettings p_i45119_2_)
    {
        invPlayingSounds = ((BiMap) playingSounds).inverse();
        categorySounds = HashMultimap.create();
        tickableSounds = Lists.newArrayList();
        delayedSounds = Maps.newHashMap();
        playingSoundsStopTime = Maps.newHashMap();
        listeners = Lists.newArrayList();
        pausedChannels = Lists.newArrayList();
        sndHandler = p_i45119_1_;
        options = p_i45119_2_;

        try
        {
            SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
            SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
        }
        catch (SoundSystemException soundsystemexception)
        {
            LOGGER.error(LOG_MARKER, "Error linking with the LibraryJavaSound plug-in", soundsystemexception);
        }
    }

    public void reloadSoundSystem()
    {
        UNABLE_TO_PLAY.clear();

        for (SoundEvent soundevent : SoundEvent.REGISTRY)
        {
            Namespaced resourcelocation = soundevent.getSoundName();

            if (sndHandler.getAccessor(resourcelocation) == null)
            {
                LOGGER.warn("Missing sound for event: {}", SoundEvent.REGISTRY.getNameForObject(soundevent));
                UNABLE_TO_PLAY.add(resourcelocation);
            }
        }

        unloadSoundSystem();
        loadSoundSystem();
    }

    /**
     * Tries to add the paulscode library and the relevant codecs. If it fails, the master volume  will be set to zero.
     */
    private synchronized void loadSoundSystem()
    {
        if (!loaded)
        {
            try
            {
                (new Thread(new Runnable()
                {
                    public void run()
                    {
                        SoundSystemConfig.setLogger(new SoundSystemLogger()
                        {
                            public void message(String p_message_1_, int p_message_2_)
                            {
                                if (!p_message_1_.isEmpty())
                                {
                                    LOGGER.info(p_message_1_);
                                }
                            }
                            public void importantMessage(String p_importantMessage_1_, int p_importantMessage_2_)
                            {
                                if (!p_importantMessage_1_.isEmpty())
                                {
                                    LOGGER.warn(p_importantMessage_1_);
                                }
                            }
                            public void errorMessage(String p_errorMessage_1_, String p_errorMessage_2_, int p_errorMessage_3_)
                            {
                                if (!p_errorMessage_2_.isEmpty())
                                {
                                    LOGGER.error("Error in class '{}'", p_errorMessage_1_);
                                    LOGGER.error(p_errorMessage_2_);
                                }
                            }
                        });
                        sndSystem = SoundManager.this.new SoundSystemStarterThread();
                        loaded = true;
                        sndSystem.setMasterVolume(options.getSoundLevel(SoundCategory.MASTER));
                        LOGGER.info(LOG_MARKER, "sound engine started");
                    }
                }, "sound Library Loader")).start();
            }
            catch (RuntimeException runtimeexception)
            {
                LOGGER.error(LOG_MARKER, "Error starting SoundSystem. Turning off sounds & music", runtimeexception);
                options.setSoundLevel(SoundCategory.MASTER, 0.0F);
                options.saveOptions();
            }
        }
    }

    private float getVolume(SoundCategory category)
    {
        return category != null && category != SoundCategory.MASTER ? options.getSoundLevel(category) : 1.0F;
    }

    public void setVolume(SoundCategory category, float volume)
    {
        if (loaded)
        {
            if (category == SoundCategory.MASTER)
            {
                sndSystem.setMasterVolume(volume);
            }
            else
            {
                for (String s : categorySounds.get(category))
                {
                    ISound isound = playingSounds.get(s);
                    float f = getClampedVolume(isound);

                    if (f <= 0.0F)
                    {
                        stopSound(isound);
                    }
                    else
                    {
                        sndSystem.setVolume(s, f);
                    }
                }
            }
        }
    }

    /**
     * Cleans up the sound System
     */
    public void unloadSoundSystem()
    {
        if (loaded)
        {
            stopAllSounds();
            sndSystem.cleanup();
            loaded = false;
        }
    }

    /**
     * Stops all currently playing sounds
     */
    public void stopAllSounds()
    {
        if (loaded)
        {
            for (String s : playingSounds.keySet())
            {
                sndSystem.stop(s);
            }

            playingSounds.clear();
            delayedSounds.clear();
            tickableSounds.clear();
            categorySounds.clear();
            playingSoundsStopTime.clear();
        }
    }

    public void addListener(ISoundEventListener listener)
    {
        listeners.add(listener);
    }

    public void removeListener(ISoundEventListener listener)
    {
        listeners.remove(listener);
    }

    public void updateAllSounds()
    {
        ++playTime;

        for (ITickableSound itickablesound : tickableSounds)
        {
            itickablesound.update();

            if (itickablesound.isDonePlaying())
            {
                stopSound(itickablesound);
            }
            else
            {
                String s = invPlayingSounds.get(itickablesound);
                sndSystem.setVolume(s, getClampedVolume(itickablesound));
                sndSystem.setPitch(s, getClampedPitch(itickablesound));
                sndSystem.setPosition(s, itickablesound.getXPosF(), itickablesound.getYPosF(), itickablesound.getZPosF());
            }
        }

        Iterator<Map.Entry<String, ISound>> iterator = playingSounds.entrySet().iterator();

        while (iterator.hasNext())
        {
            Map.Entry<String, ISound> entry = iterator.next();
            String s1 = entry.getKey();
            ISound isound = entry.getValue();

            if (!sndSystem.playing(s1))
            {
                int i = playingSoundsStopTime.get(s1).intValue();

                if (i <= playTime)
                {
                    int j = isound.getRepeatDelay();

                    if (isound.canRepeat() && j > 0)
                    {
                        delayedSounds.put(isound, Integer.valueOf(playTime + j));
                    }

                    iterator.remove();
                    LOGGER.debug(LOG_MARKER, "Removed channel {} because it's not playing anymore", s1);
                    sndSystem.removeSource(s1);
                    playingSoundsStopTime.remove(s1);

                    try
                    {
                        categorySounds.remove(isound.getCategory(), s1);
                    }
                    catch (RuntimeException var8)
                    {
                    }

                    if (isound instanceof ITickableSound)
                    {
                        tickableSounds.remove(isound);
                    }
                }
            }
        }

        Iterator<Map.Entry<ISound, Integer>> iterator1 = delayedSounds.entrySet().iterator();

        while (iterator1.hasNext())
        {
            Map.Entry<ISound, Integer> entry1 = iterator1.next();

            if (playTime >= entry1.getValue().intValue())
            {
                ISound isound1 = entry1.getKey();

                if (isound1 instanceof ITickableSound)
                {
                    ((ITickableSound)isound1).update();
                }

                playSound(isound1);
                iterator1.remove();
            }
        }
    }

    /**
     * Returns true if the sound is playing or still within time
     */
    public boolean isSoundPlaying(ISound sound)
    {
        if (!loaded)
        {
            return false;
        }
        else
        {
            String s = invPlayingSounds.get(sound);

            if (s == null)
            {
                return false;
            }
            else
            {
                return sndSystem.playing(s) || playingSoundsStopTime.containsKey(s) && playingSoundsStopTime.get(s).intValue() <= playTime;
            }
        }
    }

    public void stopSound(ISound sound)
    {
        if (loaded)
        {
            String s = invPlayingSounds.get(sound);

            if (s != null)
            {
                sndSystem.stop(s);
            }
        }
    }

    public void playSound(ISound p_sound)
    {
        if (loaded)
        {
            SoundEventAccessor soundeventaccessor = p_sound.createAccessor(sndHandler);
            Namespaced resourcelocation = p_sound.getSoundLocation();

            if (soundeventaccessor == null)
            {
                if (UNABLE_TO_PLAY.add(resourcelocation))
                {
                    LOGGER.warn(LOG_MARKER, "Unable to play unknown soundEvent: {}", resourcelocation);
                }
            }
            else
            {
                if (!listeners.isEmpty())
                {
                    for (ISoundEventListener isoundeventlistener : listeners)
                    {
                        isoundeventlistener.soundPlay(p_sound, soundeventaccessor);
                    }
                }

                if (sndSystem.getMasterVolume() <= 0.0F)
                {
                    LOGGER.debug(LOG_MARKER, "Skipped playing soundEvent: {}, master volume was zero", resourcelocation);
                }
                else
                {
                    Sound sound = p_sound.getSound();

                    if (sound == SoundHandler.MISSING_SOUND)
                    {
                        if (UNABLE_TO_PLAY.add(resourcelocation))
                        {
                            LOGGER.warn(LOG_MARKER, "Unable to play empty soundEvent: {}", resourcelocation);
                        }
                    }
                    else
                    {
                        float f3 = p_sound.getVolume();
                        float f = 16.0F;

                        if (f3 > 1.0F)
                        {
                            f *= f3;
                        }

                        SoundCategory soundcategory = p_sound.getCategory();
                        float f1 = getClampedVolume(p_sound);
                        float f2 = getClampedPitch(p_sound);

                        if (f1 == 0.0F)
                        {
                            LOGGER.debug(LOG_MARKER, "Skipped playing sound {}, volume was zero.", sound.getSoundLocation());
                        }
                        else
                        {
                            boolean flag = p_sound.canRepeat() && p_sound.getRepeatDelay() == 0;
                            String s = MathHelper.getRandomUUID(ThreadLocalRandom.current()).toString();
                            Namespaced resourcelocation1 = sound.getSoundAsOggLocation();

                            if (sound.isStreaming())
                            {
                                sndSystem.newStreamingSource(false, s, getURLForSoundResource(resourcelocation1), resourcelocation1.toString(), flag, p_sound.getXPosF(), p_sound.getYPosF(), p_sound.getZPosF(), p_sound.getAttenuationType().getTypeInt(), f);
                            }
                            else
                            {
                                sndSystem.newSource(false, s, getURLForSoundResource(resourcelocation1), resourcelocation1.toString(), flag, p_sound.getXPosF(), p_sound.getYPosF(), p_sound.getZPosF(), p_sound.getAttenuationType().getTypeInt(), f);
                            }

                            LOGGER.debug(LOG_MARKER, "Playing sound {} for event {} as channel {}", sound.getSoundLocation(), resourcelocation, s);
                            sndSystem.setPitch(s, f2);
                            sndSystem.setVolume(s, f1);
                            sndSystem.play(s);
                            playingSoundsStopTime.put(s, Integer.valueOf(playTime + 20));
                            playingSounds.put(s, p_sound);
                            categorySounds.put(soundcategory, s);

                            if (p_sound instanceof ITickableSound)
                            {
                                tickableSounds.add((ITickableSound)p_sound);
                            }
                        }
                    }
                }
            }
        }
    }

    private float getClampedPitch(ISound soundIn)
    {
        return MathHelper.clamp(soundIn.getPitch(), 0.5F, 2.0F);
    }

    private float getClampedVolume(ISound soundIn)
    {
        return MathHelper.clamp(soundIn.getVolume() * getVolume(soundIn.getCategory()), 0.0F, 1.0F);
    }

    /**
     * Pauses all currently playing sounds
     */
    public void pauseAllSounds()
    {
        for (Map.Entry<String, ISound> entry : playingSounds.entrySet())
        {
            String s = entry.getKey();
            boolean flag = isSoundPlaying(entry.getValue());

            if (flag)
            {
                LOGGER.debug(LOG_MARKER, "Pausing channel {}", s);
                sndSystem.pause(s);
                pausedChannels.add(s);
            }
        }
    }

    /**
     * Resumes playing all currently playing sounds (after pauseAllSounds)
     */
    public void resumeAllSounds()
    {
        for (String s : pausedChannels)
        {
            LOGGER.debug(LOG_MARKER, "Resuming channel {}", s);
            sndSystem.play(s);
        }

        pausedChannels.clear();
    }

    /**
     * Adds a sound to play in n tick
     */
    public void playDelayedSound(ISound sound, int delay)
    {
        delayedSounds.put(sound, Integer.valueOf(playTime + delay));
    }

    private static URL getURLForSoundResource(Namespaced p_148612_0_)
    {
        String s = String.format("%s:%s:%s", "mcsounddomain", p_148612_0_.getNamespace(), p_148612_0_.getPath());
        URLStreamHandler urlstreamhandler = new URLStreamHandler()
        {
            protected URLConnection openConnection(URL p_openConnection_1_)
            {
                return new URLConnection(p_openConnection_1_)
                {
                    public void connect() throws IOException
                    {
                    }
                    public InputStream getInputStream() throws IOException
                    {
                        return Minecraft.getResourceManager().getResource(p_148612_0_).getInputStream();
                    }
                };
            }
        };

        try
        {
            return new URL(null, s, urlstreamhandler);
        }
        catch (MalformedURLException var4)
        {
            throw new Error("TODO: Sanely handle url exception! :D");
        }
    }

    /**
     * Sets the listener of sounds
     */
    public void setListener(EntityPlayer player, float p_148615_2_)
    {
        if (loaded && player != null)
        {
            float f = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * p_148615_2_;
            float f1 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * p_148615_2_;
            double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double)p_148615_2_;
            double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double)p_148615_2_ + (double)player.getEyeHeight();
            double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)p_148615_2_;
            float f2 = MathHelper.cos((f1 + 90.0F) * 0.017453292F);
            float f3 = MathHelper.sin((f1 + 90.0F) * 0.017453292F);
            float f4 = MathHelper.cos(-f * 0.017453292F);
            float f5 = MathHelper.sin(-f * 0.017453292F);
            float f6 = MathHelper.cos((-f + 90.0F) * 0.017453292F);
            float f7 = MathHelper.sin((-f + 90.0F) * 0.017453292F);
            float f8 = f2 * f4;
            float f9 = f3 * f4;
            float f10 = f2 * f6;
            float f11 = f3 * f6;
            sndSystem.setListenerPosition((float)d0, (float)d1, (float)d2);
            sndSystem.setListenerOrientation(f8, f5, f9, f10, f7, f11);
        }
    }

    public void stop(String p_189567_1_, SoundCategory p_189567_2_)
    {
        if (p_189567_2_ != null)
        {
            for (String s : categorySounds.get(p_189567_2_))
            {
                ISound isound = playingSounds.get(s);

                if (p_189567_1_.isEmpty())
                {
                    stopSound(isound);
                }
                else if (isound.getSoundLocation().equals(new Namespaced(p_189567_1_)))
                {
                    stopSound(isound);
                }
            }
        }
        else if (p_189567_1_.isEmpty())
        {
            stopAllSounds();
        }
        else
        {
            for (ISound isound1 : playingSounds.values())
            {
                if (isound1.getSoundLocation().equals(new Namespaced(p_189567_1_)))
                {
                    stopSound(isound1);
                }
            }
        }
    }

    class SoundSystemStarterThread extends SoundSystem
    {
        private SoundSystemStarterThread()
        {
        }

        public boolean playing(String p_playing_1_)
        {
            synchronized (SoundSystemConfig.THREAD_SYNC)
            {
                if (soundLibrary == null)
                {
                    return false;
                }
                else
                {
                    Source source = soundLibrary.getSources().get(p_playing_1_);

                    if (source == null)
                    {
                        return false;
                    }
                    else
                    {
                        return source.playing() || source.paused() || source.preLoad;
                    }
                }
            }
        }
    }
}
