package net.minecraft.potion;

import com.google.common.collect.ComparisonChain;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PotionEffect implements Comparable<PotionEffect>
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final Potion potion;

    /** The duration of the potion effect */
    private int duration;

    /** The amplifier of the potion effect */
    private int amplifier;

    /** Whether the potion is a splash potion */
    private boolean isSplashPotion;

    /** Whether the potion effect came from a beacon */
    private boolean isAmbient;

    /** True if potion effect duration is at maximum, false otherwise. */
    private boolean isPotionDurationMax;
    private boolean showParticles;

    public PotionEffect(Potion potionIn)
    {
        this(potionIn, 0, 0);
    }

    public PotionEffect(Potion potionIn, int durationIn)
    {
        this(potionIn, durationIn, 0);
    }

    public PotionEffect(Potion potionIn, int durationIn, int amplifierIn)
    {
        this(potionIn, durationIn, amplifierIn, false, true);
    }

    public PotionEffect(Potion potionIn, int durationIn, int amplifierIn, boolean ambientIn, boolean showParticlesIn)
    {
        potion = potionIn;
        duration = durationIn;
        amplifier = amplifierIn;
        isAmbient = ambientIn;
        showParticles = showParticlesIn;
    }

    public PotionEffect(PotionEffect other)
    {
        potion = other.potion;
        duration = other.duration;
        amplifier = other.amplifier;
        isAmbient = other.isAmbient;
        showParticles = other.showParticles;
    }

    /**
     * merges the input PotionEffect into this one if this.amplifier <= tomerge.amplifier. The duration in the supplied
     * potion effect is assumed to be greater.
     */
    public void combine(PotionEffect other)
    {
        if (potion != other.potion)
        {
            LOGGER.warn("This method should only be called for matching effects!");
        }

        if (other.amplifier > amplifier)
        {
            amplifier = other.amplifier;
            duration = other.duration;
        }
        else if (other.amplifier == amplifier && duration < other.duration)
        {
            duration = other.duration;
        }
        else if (!other.isAmbient && isAmbient)
        {
            isAmbient = other.isAmbient;
        }

        showParticles = other.showParticles;
    }

    public Potion getPotion()
    {
        return potion;
    }

    public int getDuration()
    {
        return duration;
    }

    public int getAmplifier()
    {
        return amplifier;
    }

    /**
     * Gets whether this potion effect originated from a beacon
     */
    public boolean getIsAmbient()
    {
        return isAmbient;
    }

    /**
     * Gets whether this potion effect will show ambient particles or not.
     */
    public boolean doesShowParticles()
    {
        return showParticles;
    }

    public boolean onUpdate(EntityLivingBase entityIn)
    {
        if (duration > 0)
        {
            if (potion.isReady(duration, amplifier))
            {
                performEffect(entityIn);
            }

            deincrementDuration();
        }

        return duration > 0;
    }

    private int deincrementDuration()
    {
        return --duration;
    }

    public void performEffect(EntityLivingBase entityIn)
    {
        if (duration > 0)
        {
            potion.performEffect(entityIn, amplifier);
        }
    }

    public String getEffectName()
    {
        return potion.getName();
    }

    public String toString()
    {
        String s;

        if (amplifier > 0)
        {
            s = getEffectName() + " x " + (amplifier + 1) + ", Duration: " + duration;
        }
        else
        {
            s = getEffectName() + ", Duration: " + duration;
        }

        if (isSplashPotion)
        {
            s = s + ", Splash: true";
        }

        if (!showParticles)
        {
            s = s + ", Particles: false";
        }

        return s;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof PotionEffect))
        {
            return false;
        }
        else
        {
            PotionEffect potioneffect = (PotionEffect)p_equals_1_;
            return duration == potioneffect.duration && amplifier == potioneffect.amplifier && isSplashPotion == potioneffect.isSplashPotion && isAmbient == potioneffect.isAmbient && potion.equals(potioneffect.potion);
        }
    }

    public int hashCode()
    {
        int i = potion.hashCode();
        i = 31 * i + duration;
        i = 31 * i + amplifier;
        i = 31 * i + (isSplashPotion ? 1 : 0);
        i = 31 * i + (isAmbient ? 1 : 0);
        return i;
    }

    /**
     * Write a custom potion effect to a potion item's NBT data.
     */
    public NBTTagCompound writeCustomPotionEffectToNBT(NBTTagCompound nbt)
    {
        nbt.setByte("Id", (byte)Potion.getIdFromPotion(getPotion()));
        nbt.setByte("Amplifier", (byte) getAmplifier());
        nbt.setInteger("Duration", getDuration());
        nbt.setBoolean("Ambient", getIsAmbient());
        nbt.setBoolean("ShowParticles", doesShowParticles());
        return nbt;
    }

    /**
     * Read a custom potion effect from a potion item's NBT data.
     */
    public static PotionEffect readCustomPotionEffectFromNBT(NBTTagCompound nbt)
    {
        int i = nbt.getByte("Id");
        Potion potion = Potion.getPotionById(i);

        if (potion == null)
        {
            return null;
        }
        else
        {
            int j = nbt.getByte("Amplifier");
            int k = nbt.getInteger("Duration");
            boolean flag = nbt.getBoolean("Ambient");
            boolean flag1 = true;

            if (nbt.hasKey("ShowParticles", 1))
            {
                flag1 = nbt.getBoolean("ShowParticles");
            }

            return new PotionEffect(potion, k, j < 0 ? 0 : j, flag, flag1);
        }
    }

    /**
     * Toggle the isPotionDurationMax field.
     */
    public void setPotionDurationMax(boolean maxDuration)
    {
        isPotionDurationMax = maxDuration;
    }

    /**
     * Get the value of the isPotionDurationMax field.
     */
    public boolean getIsPotionDurationMax()
    {
        return isPotionDurationMax;
    }

    public int compareTo(PotionEffect p_compareTo_1_)
    {
        int i = 32147;
        return (getDuration() <= 32147 || p_compareTo_1_.getDuration() <= 32147) && (!getIsAmbient() || !p_compareTo_1_.getIsAmbient()) ? ComparisonChain.start().compare(Boolean.valueOf(getIsAmbient()), Boolean.valueOf(p_compareTo_1_.getIsAmbient())).compare(getDuration(), p_compareTo_1_.getDuration()).compare(getPotion().getLiquidColor(), p_compareTo_1_.getPotion().getLiquidColor()).result() : ComparisonChain.start().compare(Boolean.valueOf(getIsAmbient()), Boolean.valueOf(p_compareTo_1_.getIsAmbient())).compare(getPotion().getLiquidColor(), p_compareTo_1_.getPotion().getLiquidColor()).result();
    }
}
