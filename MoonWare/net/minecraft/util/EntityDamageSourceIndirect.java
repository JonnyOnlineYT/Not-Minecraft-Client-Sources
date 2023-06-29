package net.minecraft.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.util.text.translation.I18n;

import javax.annotation.Nullable;

public class EntityDamageSourceIndirect extends EntityDamageSource
{
    private final Entity indirectEntity;

    public EntityDamageSourceIndirect(String damageTypeIn, Entity source, @Nullable Entity indirectEntityIn)
    {
        super(damageTypeIn, source);
        indirectEntity = indirectEntityIn;
    }

    @Nullable
    public Entity getSourceOfDamage()
    {
        return damageSourceEntity;
    }

    @Nullable
    public Entity getEntity()
    {
        return indirectEntity;
    }

    /**
     * Gets the death message that is displayed when the player dies
     */
    public Component getDeathMessage(EntityLivingBase entityLivingBaseIn)
    {
        Component itextcomponent = indirectEntity == null ? damageSourceEntity.getDisplayName() : indirectEntity.getDisplayName();
        ItemStack itemstack = indirectEntity instanceof EntityLivingBase ? ((EntityLivingBase) indirectEntity).getHeldItemMainhand() : ItemStack.EMPTY;
        String s = "death.attack." + damageType;
        String s1 = s + ".item";
        return !itemstack.isEmpty() && itemstack.hasDisplayName() && I18n.canTranslate(s1) ? new TranslatableComponent(s1, entityLivingBaseIn.getDisplayName(), itextcomponent, itemstack.getTextComponent()) : new TranslatableComponent(s, entityLivingBaseIn.getDisplayName(), itextcomponent);
    }
}
