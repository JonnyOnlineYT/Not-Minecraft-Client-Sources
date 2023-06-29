package net.minecraft.util;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TranslatableComponent;

import javax.annotation.Nullable;
import java.util.List;

public class CombatTracker
{
    private final List<CombatEntry> combatEntries = Lists.newArrayList();

    /** The entity tracked. */
    private final EntityLivingBase fighter;
    private int lastDamageTime;
    private int combatStartTime;
    private int combatEndTime;
    private boolean inCombat;
    private boolean takingDamage;
    private String fallSuffix;

    public CombatTracker(EntityLivingBase fighterIn)
    {
        fighter = fighterIn;
    }

    public void calculateFallSuffix()
    {
        resetFallSuffix();

        if (fighter.isOnLadder())
        {
            Block block = fighter.world.getBlockState(new BlockPos(fighter.posX, fighter.getEntityBoundingBox().minY, fighter.posZ)).getBlock();

            if (block == Blocks.LADDER)
            {
                fallSuffix = "ladder";
            }
            else if (block == Blocks.VINE)
            {
                fallSuffix = "vines";
            }
        }
        else if (fighter.isInWater())
        {
            fallSuffix = "water";
        }
    }

    /**
     * Adds an entry for the combat tracker
     */
    public void trackDamage(DamageSource damageSrc, float healthIn, float damageAmount)
    {
        reset();
        calculateFallSuffix();
        CombatEntry combatentry = new CombatEntry(damageSrc, fighter.ticksExisted, healthIn, damageAmount, fallSuffix, fighter.fallDistance);
        combatEntries.add(combatentry);
        lastDamageTime = fighter.ticksExisted;
        takingDamage = true;

        if (combatentry.isLivingDamageSrc() && !inCombat && fighter.isEntityAlive())
        {
            inCombat = true;
            combatStartTime = fighter.ticksExisted;
            combatEndTime = combatStartTime;
            fighter.sendEnterCombat();
        }
    }

    public Component getDeathMessage()
    {
        if (combatEntries.isEmpty())
        {
            return new TranslatableComponent("death.attack.generic", fighter.getDisplayName());
        }
        else
        {
            CombatEntry combatentry = getBestCombatEntry();
            CombatEntry combatentry1 = combatEntries.get(combatEntries.size() - 1);
            Component itextcomponent1 = combatentry1.getDamageSrcDisplayName();
            Entity entity = combatentry1.getDamageSrc().getEntity();
            Component itextcomponent;

            if (combatentry != null && combatentry1.getDamageSrc() == DamageSource.fall)
            {
                Component itextcomponent2 = combatentry.getDamageSrcDisplayName();

                if (combatentry.getDamageSrc() != DamageSource.fall && combatentry.getDamageSrc() != DamageSource.outOfWorld)
                {
                    if (itextcomponent2 != null && (itextcomponent1 == null || !itextcomponent2.equals(itextcomponent1)))
                    {
                        Entity entity1 = combatentry.getDamageSrc().getEntity();
                        ItemStack itemstack1 = entity1 instanceof EntityLivingBase ? ((EntityLivingBase)entity1).getHeldItemMainhand() : ItemStack.EMPTY;

                        if (!itemstack1.isEmpty() && itemstack1.hasDisplayName())
                        {
                            itextcomponent = new TranslatableComponent("death.fell.assist.item", fighter.getDisplayName(), itextcomponent2, itemstack1.getTextComponent());
                        }
                        else
                        {
                            itextcomponent = new TranslatableComponent("death.fell.assist", fighter.getDisplayName(), itextcomponent2);
                        }
                    }
                    else if (itextcomponent1 != null)
                    {
                        ItemStack itemstack = entity instanceof EntityLivingBase ? ((EntityLivingBase)entity).getHeldItemMainhand() : ItemStack.EMPTY;

                        if (!itemstack.isEmpty() && itemstack.hasDisplayName())
                        {
                            itextcomponent = new TranslatableComponent("death.fell.finish.item", fighter.getDisplayName(), itextcomponent1, itemstack.getTextComponent());
                        }
                        else
                        {
                            itextcomponent = new TranslatableComponent("death.fell.finish", fighter.getDisplayName(), itextcomponent1);
                        }
                    }
                    else
                    {
                        itextcomponent = new TranslatableComponent("death.fell.killer", fighter.getDisplayName());
                    }
                }
                else
                {
                    itextcomponent = new TranslatableComponent("death.fell.accident." + getFallSuffix(combatentry), fighter.getDisplayName());
                }
            }
            else
            {
                itextcomponent = combatentry1.getDamageSrc().getDeathMessage(fighter);
            }

            return itextcomponent;
        }
    }

    @Nullable
    public EntityLivingBase getBestAttacker()
    {
        EntityLivingBase entitylivingbase = null;
        EntityPlayer entityplayer = null;
        float f = 0.0F;
        float f1 = 0.0F;

        for (CombatEntry combatentry : combatEntries)
        {
            if (combatentry.getDamageSrc().getEntity() instanceof EntityPlayer && (entityplayer == null || combatentry.getDamage() > f1))
            {
                f1 = combatentry.getDamage();
                entityplayer = (EntityPlayer)combatentry.getDamageSrc().getEntity();
            }

            if (combatentry.getDamageSrc().getEntity() instanceof EntityLivingBase && (entitylivingbase == null || combatentry.getDamage() > f))
            {
                f = combatentry.getDamage();
                entitylivingbase = (EntityLivingBase)combatentry.getDamageSrc().getEntity();
            }
        }

        if (entityplayer != null && f1 >= f / 3.0F)
        {
            return entityplayer;
        }
        else
        {
            return entitylivingbase;
        }
    }

    @Nullable
    private CombatEntry getBestCombatEntry()
    {
        CombatEntry combatentry = null;
        CombatEntry combatentry1 = null;
        float f = 0.0F;
        float f1 = 0.0F;

        for (int i = 0; i < combatEntries.size(); ++i)
        {
            CombatEntry combatentry2 = combatEntries.get(i);
            CombatEntry combatentry3 = i > 0 ? combatEntries.get(i - 1) : null;

            if ((combatentry2.getDamageSrc() == DamageSource.fall || combatentry2.getDamageSrc() == DamageSource.outOfWorld) && combatentry2.getDamageAmount() > 0.0F && (combatentry == null || combatentry2.getDamageAmount() > f1))
            {
                if (i > 0)
                {
                    combatentry = combatentry3;
                }
                else
                {
                    combatentry = combatentry2;
                }

                f1 = combatentry2.getDamageAmount();
            }

            if (combatentry2.getFallSuffix() != null && (combatentry1 == null || combatentry2.getDamage() > f))
            {
                combatentry1 = combatentry2;
                f = combatentry2.getDamage();
            }
        }

        if (f1 > 5.0F && combatentry != null)
        {
            return combatentry;
        }
        else if (f > 5.0F && combatentry1 != null)
        {
            return combatentry1;
        }
        else
        {
            return null;
        }
    }

    private String getFallSuffix(CombatEntry entry)
    {
        return entry.getFallSuffix() == null ? "generic" : entry.getFallSuffix();
    }

    public int getCombatDuration()
    {
        return inCombat ? fighter.ticksExisted - combatStartTime : combatEndTime - combatStartTime;
    }

    private void resetFallSuffix()
    {
        fallSuffix = null;
    }

    /**
     * Resets this trackers list of combat entries
     */
    public void reset()
    {
        int i = inCombat ? 300 : 100;

        if (takingDamage && (!fighter.isEntityAlive() || fighter.ticksExisted - lastDamageTime > i))
        {
            boolean flag = inCombat;
            takingDamage = false;
            inCombat = false;
            combatEndTime = fighter.ticksExisted;

            if (flag)
            {
                fighter.sendEndCombat();
            }

            combatEntries.clear();
        }
    }

    /**
     * Returns EntityLivingBase assigned for this CombatTracker
     */
    public EntityLivingBase getFighter()
    {
        return fighter;
    }
}
