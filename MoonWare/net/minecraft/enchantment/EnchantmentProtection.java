package net.minecraft.enchantment;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;

public class EnchantmentProtection extends Enchantment
{
    /**
     * Defines the type of protection of the enchantment, 0 = all, 1 = fire, 2 = fall (feather fall), 3 = explosion and
     * 4 = projectile.
     */
    public final EnchantmentProtection.Type protectionType;

    public EnchantmentProtection(Enchantment.Rarity rarityIn, EnchantmentProtection.Type protectionTypeIn, EntityEquipmentSlot... slots)
    {
        super(rarityIn, EnumEnchantmentType.ARMOR, slots);
        protectionType = protectionTypeIn;

        if (protectionTypeIn == EnchantmentProtection.Type.FALL)
        {
            type = EnumEnchantmentType.ARMOR_FEET;
        }
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinEnchantability(int enchantmentLevel)
    {
        return protectionType.getMinimalEnchantability() + (enchantmentLevel - 1) * protectionType.getEnchantIncreasePerLevel();
    }

    /**
     * Returns the maximum value of enchantability nedded on the enchantment level passed.
     */
    public int getMaxEnchantability(int enchantmentLevel)
    {
        return getMinEnchantability(enchantmentLevel) + protectionType.getEnchantIncreasePerLevel();
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel()
    {
        return 4;
    }

    /**
     * Calculates the damage protection of the enchantment based on level and damage source passed.
     */
    public int calcModifierDamage(int level, DamageSource source)
    {
        if (source.canHarmInCreative())
        {
            return 0;
        }
        else if (protectionType == EnchantmentProtection.Type.ALL)
        {
            return level;
        }
        else if (protectionType == EnchantmentProtection.Type.FIRE && source.isFireDamage())
        {
            return level * 2;
        }
        else if (protectionType == EnchantmentProtection.Type.FALL && source == DamageSource.fall)
        {
            return level * 3;
        }
        else if (protectionType == EnchantmentProtection.Type.EXPLOSION && source.isExplosion())
        {
            return level * 2;
        }
        else
        {
            return protectionType == EnchantmentProtection.Type.PROJECTILE && source.isProjectile() ? level * 2 : 0;
        }
    }

    /**
     * Return the name of key in translation table of this enchantment.
     */
    public String getName()
    {
        return "enchantment.protect." + protectionType.getTypeName();
    }

    /**
     * Determines if the enchantment passed can be applyied together with this enchantment.
     */
    public boolean canApplyTogether(Enchantment ench)
    {
        if (ench instanceof EnchantmentProtection)
        {
            EnchantmentProtection enchantmentprotection = (EnchantmentProtection)ench;

            if (protectionType == enchantmentprotection.protectionType)
            {
                return false;
            }
            else
            {
                return protectionType == EnchantmentProtection.Type.FALL || enchantmentprotection.protectionType == EnchantmentProtection.Type.FALL;
            }
        }
        else
        {
            return super.canApplyTogether(ench);
        }
    }

    /**
     * Gets the amount of ticks an entity should be set fire, adjusted for fire protection.
     */
    public static int getFireTimeForEntity(EntityLivingBase p_92093_0_, int p_92093_1_)
    {
        int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FIRE_PROTECTION, p_92093_0_);

        if (i > 0)
        {
            p_92093_1_ -= MathHelper.floor((float)p_92093_1_ * (float)i * 0.15F);
        }

        return p_92093_1_;
    }

    public static double getBlastDamageReduction(EntityLivingBase entityLivingBaseIn, double damage)
    {
        int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.BLAST_PROTECTION, entityLivingBaseIn);

        if (i > 0)
        {
            damage -= MathHelper.floor(damage * (double)((float)i * 0.15F));
        }

        return damage;
    }

    public enum Type
    {
        ALL("all", 1, 11, 20),
        FIRE("fire", 10, 8, 12),
        FALL("fall", 5, 6, 10),
        EXPLOSION("explosion", 5, 8, 12),
        PROJECTILE("projectile", 3, 6, 15);

        private final String typeName;
        private final int minEnchantability;
        private final int levelCost;
        private final int levelCostSpan;

        Type(String name, int minimal, int perLevelEnchantability, int p_i47051_6_)
        {
            typeName = name;
            minEnchantability = minimal;
            levelCost = perLevelEnchantability;
            levelCostSpan = p_i47051_6_;
        }

        public String getTypeName()
        {
            return typeName;
        }

        public int getMinimalEnchantability()
        {
            return minEnchantability;
        }

        public int getEnchantIncreasePerLevel()
        {
            return levelCost;
        }
    }
}
