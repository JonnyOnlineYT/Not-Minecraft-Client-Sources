package net.minecraft.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.util.text.translation.I18n;

import javax.annotation.Nullable;

public class EntityDamageSource extends DamageSource
{
    @Nullable
    protected Entity damageSourceEntity;

    /**
     * Whether this EntityDamageSource is from an entity wearing Thorns-enchanted armor.
     */
    private boolean isThornsDamage;

    public EntityDamageSource(String damageTypeIn, @Nullable Entity damageSourceEntityIn)
    {
        super(damageTypeIn);
        damageSourceEntity = damageSourceEntityIn;
    }

    /**
     * Sets this EntityDamageSource as originating from Thorns armor
     */
    public EntityDamageSource setIsThornsDamage()
    {
        isThornsDamage = true;
        return this;
    }

    public boolean getIsThornsDamage()
    {
        return isThornsDamage;
    }

    @Nullable
    public Entity getEntity()
    {
        return damageSourceEntity;
    }

    /**
     * Gets the death message that is displayed when the player dies
     */
    public Component getDeathMessage(EntityLivingBase entityLivingBaseIn)
    {
        ItemStack itemstack = damageSourceEntity instanceof EntityLivingBase ? ((EntityLivingBase) damageSourceEntity).getHeldItemMainhand() : ItemStack.EMPTY;
        String s = "death.attack." + damageType;
        String s1 = s + ".item";
        return !itemstack.isEmpty() && itemstack.hasDisplayName() && I18n.canTranslate(s1) ? new TranslatableComponent(s1, entityLivingBaseIn.getDisplayName(), damageSourceEntity.getDisplayName(), itemstack.getTextComponent()) : new TranslatableComponent(s, entityLivingBaseIn.getDisplayName(), damageSourceEntity.getDisplayName());
    }

    /**
     * Return whether this damage source will have its damage amount scaled based on the current difficulty.
     */
    public boolean isDifficultyScaled()
    {
        return damageSourceEntity != null && damageSourceEntity instanceof EntityLivingBase && !(damageSourceEntity instanceof EntityPlayer);
    }

    @Nullable

    /**
     * Gets the location from which the damage originates.
     */
    public Vec3d getDamageLocation()
    {
        return new Vec3d(damageSourceEntity.posX, damageSourceEntity.posY, damageSourceEntity.posZ);
    }
}
