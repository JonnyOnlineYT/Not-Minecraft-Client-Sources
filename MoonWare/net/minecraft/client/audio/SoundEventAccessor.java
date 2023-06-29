package net.minecraft.client.audio;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.util.Namespaced;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TranslatableComponent;

public class SoundEventAccessor implements ISoundEventAccessor<Sound>
{
    private final List<ISoundEventAccessor<Sound>> accessorList = Lists.newArrayList();
    private final Random rnd = new Random();
    private final Namespaced location;
    private final Component subtitle;

    public SoundEventAccessor(Namespaced locationIn, @Nullable String subtitleIn)
    {
        location = locationIn;
        subtitle = subtitleIn == null ? null : new TranslatableComponent(subtitleIn);
    }

    public int getWeight()
    {
        int i = 0;

        for (ISoundEventAccessor<Sound> isoundeventaccessor : accessorList)
        {
            i += isoundeventaccessor.getWeight();
        }

        return i;
    }

    public Sound cloneEntry()
    {
        int i = getWeight();

        if (!accessorList.isEmpty() && i != 0)
        {
            int j = rnd.nextInt(i);

            for (ISoundEventAccessor<Sound> isoundeventaccessor : accessorList)
            {
                j -= isoundeventaccessor.getWeight();

                if (j < 0)
                {
                    return isoundeventaccessor.cloneEntry();
                }
            }

            return SoundHandler.MISSING_SOUND;
        }
        else
        {
            return SoundHandler.MISSING_SOUND;
        }
    }

    public void addSound(ISoundEventAccessor<Sound> p_188715_1_)
    {
        accessorList.add(p_188715_1_);
    }

    public Namespaced getLocation()
    {
        return location;
    }

    @Nullable
    public Component getSubtitle()
    {
        return subtitle;
    }
}
