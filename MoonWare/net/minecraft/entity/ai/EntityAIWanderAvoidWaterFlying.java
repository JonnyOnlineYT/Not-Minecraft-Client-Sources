package net.minecraft.entity.ai;

import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.entity.EntityCreature;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class EntityAIWanderAvoidWaterFlying extends EntityAIWanderAvoidWater
{
    public EntityAIWanderAvoidWaterFlying(EntityCreature p_i47413_1_, double p_i47413_2_)
    {
        super(p_i47413_1_, p_i47413_2_);
    }

    @Nullable
    protected Vec3d func_190864_f()
    {
        Vec3d vec3d = null;

        if (entity.isInWater() || entity.func_191953_am())
        {
            vec3d = RandomPositionGenerator.func_191377_b(entity, 15, 15);
        }

        if (entity.getRNG().nextFloat() >= field_190865_h)
        {
            vec3d = func_192385_j();
        }

        return vec3d == null ? super.func_190864_f() : vec3d;
    }

    @Nullable
    private Vec3d func_192385_j()
    {
        BlockPos blockpos = new BlockPos(entity);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos blockpos$mutableblockpos1 = new BlockPos.MutableBlockPos();
        Iterable<BlockPos.MutableBlockPos> iterable = BlockPos.func_191531_b(MathHelper.floor(entity.posX - 3.0D), MathHelper.floor(entity.posY - 6.0D), MathHelper.floor(entity.posZ - 3.0D), MathHelper.floor(entity.posX + 3.0D), MathHelper.floor(entity.posY + 6.0D), MathHelper.floor(entity.posZ + 3.0D));
        Iterator iterator = iterable.iterator();
        BlockPos blockpos1;

        while (true)
        {
            if (!iterator.hasNext())
            {
                return null;
            }

            blockpos1 = (BlockPos)iterator.next();

            if (!blockpos.equals(blockpos1))
            {
                Block block = entity.world.getBlockState(blockpos$mutableblockpos1.setPos(blockpos1).move(EnumFacing.DOWN)).getBlock();
                boolean flag = block instanceof BlockLeaves || block == Blocks.LOG || block == Blocks.LOG2;

                if (flag && entity.world.isAirBlock(blockpos1) && entity.world.isAirBlock(blockpos$mutableblockpos.setPos(blockpos1).move(EnumFacing.UP)))
                {
                    break;
                }
            }
        }

        return new Vec3d(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
    }
}
