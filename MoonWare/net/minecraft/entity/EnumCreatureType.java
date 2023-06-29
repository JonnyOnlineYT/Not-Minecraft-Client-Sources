package net.minecraft.entity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.IAnimals;

public enum EnumCreatureType
{
    MONSTER(IMob.class, 70, Material.AIR, false, false),
    CREATURE(EntityAnimal.class, 10, Material.AIR, true, true),
    AMBIENT(EntityAmbientCreature.class, 15, Material.AIR, true, false),
    WATER_CREATURE(EntityWaterMob.class, 5, Material.WATER, true, false);

    private final Class <? extends IAnimals > creatureClass;
    private final int maxNumberOfCreature;
    private final Material creatureMaterial;

    /** A flag indicating whether this creature type is peaceful. */
    private final boolean isPeacefulCreature;

    /** Whether this creature type is an animal. */
    private final boolean isAnimal;

    EnumCreatureType(Class <? extends IAnimals > creatureClassIn, int maxNumberOfCreatureIn, Material creatureMaterialIn, boolean isPeacefulCreatureIn, boolean isAnimalIn)
    {
        creatureClass = creatureClassIn;
        maxNumberOfCreature = maxNumberOfCreatureIn;
        creatureMaterial = creatureMaterialIn;
        isPeacefulCreature = isPeacefulCreatureIn;
        isAnimal = isAnimalIn;
    }

    public Class <? extends IAnimals > getCreatureClass()
    {
        return creatureClass;
    }

    public int getMaxNumberOfCreature()
    {
        return maxNumberOfCreature;
    }

    /**
     * Gets whether or not this creature type is peaceful.
     */
    public boolean getPeacefulCreature()
    {
        return isPeacefulCreature;
    }

    /**
     * Return whether this creature type is an animal.
     */
    public boolean getAnimal()
    {
        return isAnimal;
    }
}
