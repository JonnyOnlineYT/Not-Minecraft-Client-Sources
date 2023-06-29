package net.minecraft.client.audio;

import net.minecraft.util.Namespaced;

public class Sound implements ISoundEventAccessor<Sound>
{
    private final Namespaced name;
    private final float volume;
    private final float pitch;
    private final int weight;
    private final Sound.Type type;
    private final boolean streaming;

    public Sound(String nameIn, float volumeIn, float pitchIn, int weightIn, Sound.Type typeIn, boolean p_i46526_6_)
    {
        name = new Namespaced(nameIn);
        volume = volumeIn;
        pitch = pitchIn;
        weight = weightIn;
        type = typeIn;
        streaming = p_i46526_6_;
    }

    public Namespaced getSoundLocation()
    {
        return name;
    }

    public Namespaced getSoundAsOggLocation()
    {
        return new Namespaced(name.getNamespace(), "sounds/" + name.getPath() + ".ogg");
    }

    public float getVolume()
    {
        return volume;
    }

    public float getPitch()
    {
        return pitch;
    }

    public int getWeight()
    {
        return weight;
    }

    public Sound cloneEntry()
    {
        return this;
    }

    public Sound.Type getType()
    {
        return type;
    }

    public boolean isStreaming()
    {
        return streaming;
    }

    public enum Type
    {
        FILE("file"),
        SOUND_EVENT("event");

        private final String name;

        Type(String nameIn)
        {
            name = nameIn;
        }

        public static Sound.Type getByName(String nameIn)
        {
            for (Sound.Type sound$type : values())
            {
                if (sound$type.name.equals(nameIn))
                {
                    return sound$type;
                }
            }

            return null;
        }
    }
}
