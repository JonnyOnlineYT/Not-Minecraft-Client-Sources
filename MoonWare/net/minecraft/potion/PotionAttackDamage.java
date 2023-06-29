package net.minecraft.potion;

import net.minecraft.entity.ai.attributes.AttributeModifier;

public class PotionAttackDamage extends Potion
{
    protected final double bonusPerLevel;

    protected PotionAttackDamage(boolean isBadEffectIn, int liquidColorIn, double bonusPerLevelIn)
    {
        super(isBadEffectIn, liquidColorIn);
        bonusPerLevel = bonusPerLevelIn;
    }

    public double getAttributeModifierAmount(int amplifier, AttributeModifier modifier)
    {
        return bonusPerLevel * (double)(amplifier + 1);
    }
}
