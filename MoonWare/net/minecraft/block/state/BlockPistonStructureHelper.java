package net.minecraft.block.state;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class BlockPistonStructureHelper
{
    private final World world;
    private final BlockPos pistonPos;
    private final BlockPos blockToMove;
    private final EnumFacing moveDirection;
    private final List<BlockPos> toMove = Lists.newArrayList();
    private final List<BlockPos> toDestroy = Lists.newArrayList();

    public BlockPistonStructureHelper(World worldIn, BlockPos posIn, EnumFacing pistonFacing, boolean extending)
    {
        world = worldIn;
        pistonPos = posIn;

        if (extending)
        {
            moveDirection = pistonFacing;
            blockToMove = posIn.offset(pistonFacing);
        }
        else
        {
            moveDirection = pistonFacing.getOpposite();
            blockToMove = posIn.offset(pistonFacing, 2);
        }
    }

    public boolean canMove()
    {
        toMove.clear();
        toDestroy.clear();
        IBlockState iblockstate = world.getBlockState(blockToMove);

        if (!BlockPistonBase.canPush(iblockstate, world, blockToMove, moveDirection, false, moveDirection))
        {
            if (iblockstate.getMobilityFlag() == EnumPushReaction.DESTROY)
            {
                toDestroy.add(blockToMove);
                return true;
            }
            else
            {
                return false;
            }
        }
        else if (!addBlockLine(blockToMove, moveDirection))
        {
            return false;
        }
        else
        {
            for (int i = 0; i < toMove.size(); ++i)
            {
                BlockPos blockpos = toMove.get(i);

                if (world.getBlockState(blockpos).getBlock() == Blocks.SLIME_BLOCK && !addBranchingBlocks(blockpos))
                {
                    return false;
                }
            }

            return true;
        }
    }

    private boolean addBlockLine(BlockPos origin, EnumFacing p_177251_2_)
    {
        IBlockState iblockstate = world.getBlockState(origin);
        Block block = iblockstate.getBlock();

        if (iblockstate.getMaterial() == Material.AIR)
        {
            return true;
        }
        else if (!BlockPistonBase.canPush(iblockstate, world, origin, moveDirection, false, p_177251_2_))
        {
            return true;
        }
        else if (origin.equals(pistonPos))
        {
            return true;
        }
        else if (toMove.contains(origin))
        {
            return true;
        }
        else
        {
            int i = 1;

            if (i + toMove.size() > 12)
            {
                return false;
            }
            else
            {
                while (block == Blocks.SLIME_BLOCK)
                {
                    BlockPos blockpos = origin.offset(moveDirection.getOpposite(), i);
                    iblockstate = world.getBlockState(blockpos);
                    block = iblockstate.getBlock();

                    if (iblockstate.getMaterial() == Material.AIR || !BlockPistonBase.canPush(iblockstate, world, blockpos, moveDirection, false, moveDirection.getOpposite()) || blockpos.equals(pistonPos))
                    {
                        break;
                    }

                    ++i;

                    if (i + toMove.size() > 12)
                    {
                        return false;
                    }
                }

                int i1 = 0;

                for (int j = i - 1; j >= 0; --j)
                {
                    toMove.add(origin.offset(moveDirection.getOpposite(), j));
                    ++i1;
                }

                int j1 = 1;

                while (true)
                {
                    BlockPos blockpos1 = origin.offset(moveDirection, j1);
                    int k = toMove.indexOf(blockpos1);

                    if (k > -1)
                    {
                        reorderListAtCollision(i1, k);

                        for (int l = 0; l <= k + i1; ++l)
                        {
                            BlockPos blockpos2 = toMove.get(l);

                            if (world.getBlockState(blockpos2).getBlock() == Blocks.SLIME_BLOCK && !addBranchingBlocks(blockpos2))
                            {
                                return false;
                            }
                        }

                        return true;
                    }

                    iblockstate = world.getBlockState(blockpos1);

                    if (iblockstate.getMaterial() == Material.AIR)
                    {
                        return true;
                    }

                    if (!BlockPistonBase.canPush(iblockstate, world, blockpos1, moveDirection, true, moveDirection) || blockpos1.equals(pistonPos))
                    {
                        return false;
                    }

                    if (iblockstate.getMobilityFlag() == EnumPushReaction.DESTROY)
                    {
                        toDestroy.add(blockpos1);
                        return true;
                    }

                    if (toMove.size() >= 12)
                    {
                        return false;
                    }

                    toMove.add(blockpos1);
                    ++i1;
                    ++j1;
                }
            }
        }
    }

    private void reorderListAtCollision(int p_177255_1_, int p_177255_2_)
    {
        List<BlockPos> list = Lists.newArrayList();
        List<BlockPos> list1 = Lists.newArrayList();
        List<BlockPos> list2 = Lists.newArrayList();
        list.addAll(toMove.subList(0, p_177255_2_));
        list1.addAll(toMove.subList(toMove.size() - p_177255_1_, toMove.size()));
        list2.addAll(toMove.subList(p_177255_2_, toMove.size() - p_177255_1_));
        toMove.clear();
        toMove.addAll(list);
        toMove.addAll(list1);
        toMove.addAll(list2);
    }

    private boolean addBranchingBlocks(BlockPos p_177250_1_)
    {
        for (EnumFacing enumfacing : EnumFacing.values())
        {
            if (enumfacing.getAxis() != moveDirection.getAxis() && !addBlockLine(p_177250_1_.offset(enumfacing), enumfacing))
            {
                return false;
            }
        }

        return true;
    }

    public List<BlockPos> getBlocksToMove()
    {
        return toMove;
    }

    public List<BlockPos> getBlocksToDestroy()
    {
        return toDestroy;
    }
}
