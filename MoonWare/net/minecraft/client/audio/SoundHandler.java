package net.minecraft.client.audio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ITickable;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.Namespaced;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TranslatableComponent;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SoundHandler implements IResourceManagerReloadListener, ITickable
{
    public static final Sound MISSING_SOUND = new Sound("meta:missing_sound", 1.0F, 1.0F, 1, Sound.Type.FILE, false);
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).registerTypeHierarchyAdapter(Component.class, new Component.Serializer()).registerTypeAdapter(SoundList.class, new SoundListSerializer()).create();
    private static final ParameterizedType TYPE = new ParameterizedType()
    {
        public Type[] getActualTypeArguments()
        {
            return new Type[] {String.class, SoundList.class};
        }
        public Type getRawType()
        {
            return Map.class;
        }
        public Type getOwnerType()
        {
            return null;
        }
    };
    private final SoundRegistry soundRegistry = new SoundRegistry();
    private final SoundManager sndManager;
    private final IResourceManager mcResourceManager;

    public SoundHandler(IResourceManager manager, GameSettings gameSettingsIn)
    {
        mcResourceManager = manager;
        sndManager = new SoundManager(this, gameSettingsIn);
    }

    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        soundRegistry.clearMap();

        for (String s : resourceManager.getResourceDomains())
        {
            try
            {
                for (IResource iresource : resourceManager.getAllResources(new Namespaced(s, "sounds.json")))
                {
                    try
                    {
                        Map<String, SoundList> map = getSoundMap(iresource.getInputStream());

                        for (Map.Entry<String, SoundList> entry : map.entrySet())
                        {
                            loadSoundResource(new Namespaced(s, entry.getKey()), entry.getValue());
                        }
                    }
                    catch (RuntimeException runtimeexception)
                    {
                        LOGGER.warn("Invalid sounds.json", runtimeexception);
                    }
                }
            }
            catch (IOException var11)
            {
            }
        }

        for (Namespaced resourcelocation : soundRegistry.getKeys())
        {
            SoundEventAccessor soundeventaccessor = soundRegistry.getObject(resourcelocation);

            if (soundeventaccessor.getSubtitle() instanceof TranslatableComponent)
            {
                String s1 = ((TranslatableComponent)soundeventaccessor.getSubtitle()).getKey();

                if (!I18n.hasKey(s1))
                {
                    LOGGER.debug("Missing subtitle {} for event: {}", s1, resourcelocation);
                }
            }
        }

        for (Namespaced resourcelocation1 : soundRegistry.getKeys())
        {
            if (SoundEvent.REGISTRY.getObject(resourcelocation1) == null)
            {
                LOGGER.debug("Not having sound event for: {}", resourcelocation1);
            }
        }

        sndManager.reloadSoundSystem();
    }

    @Nullable
    protected Map<String, SoundList> getSoundMap(InputStream stream)
    {
        Map map;

        try
        {
            map = JsonUtils.func_193841_a(GSON, new InputStreamReader(stream, StandardCharsets.UTF_8), TYPE);
        }
        finally
        {
            IOUtils.closeQuietly(stream);
        }

        return map;
    }

    private void loadSoundResource(Namespaced location, SoundList sounds)
    {
        SoundEventAccessor soundeventaccessor = soundRegistry.getObject(location);
        boolean flag = soundeventaccessor == null;

        if (flag || sounds.canReplaceExisting())
        {
            if (!flag)
            {
                LOGGER.debug("Replaced sound event location {}", location);
            }

            soundeventaccessor = new SoundEventAccessor(location, sounds.getSubtitle());
            soundRegistry.add(soundeventaccessor);
        }

        for (Sound sound : sounds.getSounds())
        {
            Namespaced resourcelocation = sound.getSoundLocation();
            ISoundEventAccessor<Sound> isoundeventaccessor;

            switch (sound.getType())
            {
                case FILE:
                    if (!validateSoundResource(sound, location))
                    {
                        continue;
                    }

                    isoundeventaccessor = sound;
                    break;

                case SOUND_EVENT:
                    isoundeventaccessor = new ISoundEventAccessor<Sound>()
                    {
                        public int getWeight()
                        {
                            SoundEventAccessor soundeventaccessor1 = soundRegistry.getObject(resourcelocation);
                            return soundeventaccessor1 == null ? 0 : soundeventaccessor1.getWeight();
                        }
                        public Sound cloneEntry()
                        {
                            SoundEventAccessor soundeventaccessor1 = soundRegistry.getObject(resourcelocation);

                            if (soundeventaccessor1 == null)
                            {
                                return MISSING_SOUND;
                            }
                            else
                            {
                                Sound sound1 = soundeventaccessor1.cloneEntry();
                                return new Sound(sound1.getSoundLocation().toString(), sound1.getVolume() * sound.getVolume(), sound1.getPitch() * sound.getPitch(), sound.getWeight(), Sound.Type.FILE, sound1.isStreaming() || sound.isStreaming());
                            }
                        }
                    };

                    break;
                default:
                    throw new IllegalStateException("Unknown SoundEventRegistration type: " + sound.getType());
            }

            soundeventaccessor.addSound(isoundeventaccessor);
        }
    }

    private boolean validateSoundResource(Sound p_184401_1_, Namespaced p_184401_2_)
    {
        Namespaced resourcelocation = p_184401_1_.getSoundAsOggLocation();
        IResource iresource = null;
        boolean flag;

        try
        {
            iresource = mcResourceManager.getResource(resourcelocation);
            iresource.getInputStream();
            return true;
        }
        catch (FileNotFoundException var11)
        {
            LOGGER.warn("File {} does not exist, cannot add it to event {}", resourcelocation, p_184401_2_);
            flag = false;
        }
        catch (IOException ioexception)
        {
            LOGGER.warn("Could not load sound file {}, cannot add it to event {}", resourcelocation, p_184401_2_, ioexception);
            flag = false;
            return flag;
        }
        finally
        {
            IOUtils.closeQuietly(iresource);
        }

        return flag;
    }

    @Nullable
    public SoundEventAccessor getAccessor(Namespaced location)
    {
        return soundRegistry.getObject(location);
    }

    /**
     * Play a sound
     */
    public void playSound(ISound sound)
    {
        sndManager.playSound(sound);
    }

    /**
     * Plays the sound in n ticks
     */
    public void playDelayedSound(ISound sound, int delay)
    {
        sndManager.playDelayedSound(sound, delay);
    }

    public void setListener(EntityPlayer player, float p_147691_2_)
    {
        sndManager.setListener(player, p_147691_2_);
    }

    public void pauseSounds()
    {
        sndManager.pauseAllSounds();
    }

    public void stopSounds()
    {
        sndManager.stopAllSounds();
    }

    public void unloadSounds()
    {
        sndManager.unloadSoundSystem();
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    public void update()
    {
        sndManager.updateAllSounds();
    }

    public void resumeSounds()
    {
        sndManager.resumeAllSounds();
    }

    public void setSoundLevel(SoundCategory category, float volume)
    {
        if (category == SoundCategory.MASTER && volume <= 0.0F)
        {
            stopSounds();
        }

        sndManager.setVolume(category, volume);
    }

    public void stopSound(ISound soundIn)
    {
        sndManager.stopSound(soundIn);
    }

    public boolean isSoundPlaying(ISound sound)
    {
        return sndManager.isSoundPlaying(sound);
    }

    public void addListener(ISoundEventListener listener)
    {
        sndManager.addListener(listener);
    }

    public void removeListener(ISoundEventListener listener)
    {
        sndManager.removeListener(listener);
    }

    public void stop(String p_189520_1_, SoundCategory p_189520_2_)
    {
        sndManager.stop(p_189520_1_, p_189520_2_);
    }
}
