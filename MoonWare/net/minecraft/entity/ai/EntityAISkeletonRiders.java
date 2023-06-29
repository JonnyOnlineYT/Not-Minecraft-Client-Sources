package net.minecraft.entity.ai;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;

public class EntityAISkeletonRiders extends EntityAIBase
{
    private final EntitySkeletonHorse horse;

    public EntityAISkeletonRiders(EntitySkeletonHorse horseIn)
    {
        horse = horseIn;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        return horse.world.isAnyPlayerWithinRangeAt(horse.posX, horse.posY, horse.posZ, 10.0D);
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        DifficultyInstance difficultyinstance = horse.world.getDifficultyForLocation(new BlockPos(horse));
        horse.func_190691_p(false);
        horse.setHorseTamed(true);
        horse.setGrowingAge(0);
        horse.world.addWeatherEffect(new EntityLightningBolt(horse.world, horse.posX, horse.posY, horse.posZ, true));
        EntitySkeleton entityskeleton = createSkeleton(difficultyinstance, horse);
        entityskeleton.startRiding(horse);

        for (int i = 0; i < 3; ++i)
        {
            AbstractHorse abstracthorse = createHorse(difficultyinstance);
            EntitySkeleton entityskeleton1 = createSkeleton(difficultyinstance, abstracthorse);
            entityskeleton1.startRiding(abstracthorse);
            abstracthorse.addVelocity(horse.getRNG().nextGaussian() * 0.5D, 0.0D, horse.getRNG().nextGaussian() * 0.5D);
        }
    }

    private AbstractHorse createHorse(DifficultyInstance p_188515_1_)
    {
        EntitySkeletonHorse entityskeletonhorse = new EntitySkeletonHorse(horse.world);
        entityskeletonhorse.onInitialSpawn(p_188515_1_, null);
        entityskeletonhorse.setPosition(horse.posX, horse.posY, horse.posZ);
        entityskeletonhorse.hurtResistantTime = 60;
        entityskeletonhorse.enablePersistence();
        entityskeletonhorse.setHorseTamed(true);
        entityskeletonhorse.setGrowingAge(0);
        entityskeletonhorse.world.spawnEntityInWorld(entityskeletonhorse);
        return entityskeletonhorse;
    }

    private EntitySkeleton createSkeleton(DifficultyInstance p_188514_1_, AbstractHorse p_188514_2_)
    {
        EntitySkeleton entityskeleton = new EntitySkeleton(p_188514_2_.world);
        entityskeleton.onInitialSpawn(p_188514_1_, null);
        entityskeleton.setPosition(p_188514_2_.posX, p_188514_2_.posY, p_188514_2_.posZ);
        entityskeleton.hurtResistantTime = 60;
        entityskeleton.enablePersistence();

        if (entityskeleton.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty())
        {
            entityskeleton.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        }

        entityskeleton.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, EnchantmentHelper.addRandomEnchantment(entityskeleton.getRNG(), entityskeleton.getHeldItemMainhand(), (int)(5.0F + p_188514_1_.getClampedAdditionalDifficulty() * (float)entityskeleton.getRNG().nextInt(18)), false));
        entityskeleton.setItemStackToSlot(EntityEquipmentSlot.HEAD, EnchantmentHelper.addRandomEnchantment(entityskeleton.getRNG(), entityskeleton.getItemStackFromSlot(EntityEquipmentSlot.HEAD), (int)(5.0F + p_188514_1_.getClampedAdditionalDifficulty() * (float)entityskeleton.getRNG().nextInt(18)), false));
        entityskeleton.world.spawnEntityInWorld(entityskeleton);
        return entityskeleton;
    }
}
