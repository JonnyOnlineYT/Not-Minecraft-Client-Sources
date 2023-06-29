package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAIEatGrass extends EntityAIBase
{
    private static final Predicate<IBlockState> IS_TALL_GRASS = BlockStateMatcher.forBlock(Blocks.TALLGRASS).where(BlockTallGrass.TYPE, Predicates.equalTo(BlockTallGrass.EnumType.GRASS));

    /** The entity owner of this AITask */
    private final EntityLiving grassEaterEntity;

    /** The world the grass eater entity is eating from */
    private final World entityWorld;

    /** Number of ticks since the entity started to eat grass */
    int eatingGrassTimer;

    public EntityAIEatGrass(EntityLiving grassEaterEntityIn)
    {
        grassEaterEntity = grassEaterEntityIn;
        entityWorld = grassEaterEntityIn.world;
        setMutexBits(7);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (grassEaterEntity.getRNG().nextInt(grassEaterEntity.isChild() ? 50 : 1000) != 0)
        {
            return false;
        }
        else
        {
            BlockPos blockpos = new BlockPos(grassEaterEntity.posX, grassEaterEntity.posY, grassEaterEntity.posZ);

            if (IS_TALL_GRASS.apply(entityWorld.getBlockState(blockpos)))
            {
                return true;
            }
            else
            {
                return entityWorld.getBlockState(blockpos.down()).getBlock() == Blocks.GRASS;
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        eatingGrassTimer = 40;
        entityWorld.setEntityState(grassEaterEntity, (byte)10);
        grassEaterEntity.getNavigator().clearPathEntity();
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        eatingGrassTimer = 0;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return eatingGrassTimer > 0;
    }

    /**
     * Number of ticks since the entity started to eat grass
     */
    public int getEatingGrassTimer()
    {
        return eatingGrassTimer;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        eatingGrassTimer = Math.max(0, eatingGrassTimer - 1);

        if (eatingGrassTimer == 4)
        {
            BlockPos blockpos = new BlockPos(grassEaterEntity.posX, grassEaterEntity.posY, grassEaterEntity.posZ);

            if (IS_TALL_GRASS.apply(entityWorld.getBlockState(blockpos)))
            {
                if (entityWorld.getGameRules().getBoolean("mobGriefing"))
                {
                    entityWorld.destroyBlock(blockpos, false);
                }

                grassEaterEntity.eatGrassBonus();
            }
            else
            {
                BlockPos blockpos1 = blockpos.down();

                if (entityWorld.getBlockState(blockpos1).getBlock() == Blocks.GRASS)
                {
                    if (entityWorld.getGameRules().getBoolean("mobGriefing"))
                    {
                        entityWorld.playEvent(2001, blockpos1, Block.getIdFromBlock(Blocks.GRASS));
                        entityWorld.setBlockState(blockpos1, Blocks.DIRT.getDefaultState(), 2);
                    }

                    grassEaterEntity.eatGrassBonus();
                }
            }
        }
    }
}
