package net.minecraft.client.audio;

import javax.annotation.Nullable;
import net.minecraft.util.Namespaced;
import net.minecraft.util.SoundCategory;

public interface ISound
{
    Namespaced getSoundLocation();

    @Nullable
    SoundEventAccessor createAccessor(SoundHandler handler);

    Sound getSound();

    SoundCategory getCategory();

    boolean canRepeat();

    int getRepeatDelay();

    float getVolume();

    float getPitch();

    float getXPosF();

    float getYPosF();

    float getZPosF();

    ISound.AttenuationType getAttenuationType();

    enum AttenuationType
    {
        NONE(0),
        LINEAR(2);

        private final int type;

        AttenuationType(int typeIn)
        {
            type = typeIn;
        }

        public int getTypeInt()
        {
            return type;
        }
    }
}
