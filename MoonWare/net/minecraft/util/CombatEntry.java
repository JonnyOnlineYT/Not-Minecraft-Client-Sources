package net.minecraft.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.text.Component;

import javax.annotation.Nullable;

public class CombatEntry
{
    private final DamageSource damageSrc;
    private final int time;
    private final float damage;
    private final float health;
    private final String fallSuffix;
    private final float fallDistance;

    public CombatEntry(DamageSource damageSrcIn, int p_i1564_2_, float healthAmount, float damageAmount, String fallSuffixIn, float fallDistanceIn)
    {
        damageSrc = damageSrcIn;
        time = p_i1564_2_;
        damage = damageAmount;
        health = healthAmount;
        fallSuffix = fallSuffixIn;
        fallDistance = fallDistanceIn;
    }

    /**
     * Get the DamageSource of the CombatEntry instance.
     */
    public DamageSource getDamageSrc()
    {
        return damageSrc;
    }

    public float getDamage()
    {
        return damage;
    }

    /**
     * Returns true if {@link net.minecraft.util.DamageSource#getEntity() damage source} is a living entity
     */
    public boolean isLivingDamageSrc()
    {
        return damageSrc.getEntity() instanceof EntityLivingBase;
    }

    @Nullable
    public String getFallSuffix()
    {
        return fallSuffix;
    }

    @Nullable
    public Component getDamageSrcDisplayName()
    {
        return getDamageSrc().getEntity() == null ? null : getDamageSrc().getEntity().getDisplayName();
    }

    public float getDamageAmount()
    {
        return damageSrc == DamageSource.outOfWorld ? Float.MAX_VALUE : fallDistance;
    }
}
