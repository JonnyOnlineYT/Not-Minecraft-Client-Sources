package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

public class BlockDynamicLiquid extends BlockLiquid
{
    int adjacentSourceBlocks;

    protected BlockDynamicLiquid(Material materialIn)
    {
        super(materialIn);
    }

    private void placeStaticBlock(World worldIn, BlockPos pos, IBlockState currentState)
    {
        worldIn.setBlockState(pos, BlockLiquid.getStaticBlock(blockMaterial).getDefaultState().withProperty(BlockLiquid.LEVEL, currentState.getValue(BlockLiquid.LEVEL)), 2);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        int i = state.getValue(BlockLiquid.LEVEL).intValue();
        int j = 1;

        if (blockMaterial == Material.LAVA && !worldIn.provider.doesWaterVaporize())
        {
            j = 2;
        }

        int k = tickRate(worldIn);

        if (i > 0)
        {
            int l = -100;
            adjacentSourceBlocks = 0;

            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
            {
                l = checkAdjacentBlock(worldIn, pos.offset(enumfacing), l);
            }

            int i1 = l + j;

            if (i1 >= 8 || l < 0)
            {
                i1 = -1;
            }

            int j1 = getDepth(worldIn.getBlockState(pos.up()));

            if (j1 >= 0)
            {
                if (j1 >= 8)
                {
                    i1 = j1;
                }
                else
                {
                    i1 = j1 + 8;
                }
            }

            if (adjacentSourceBlocks >= 2 && blockMaterial == Material.WATER)
            {
                IBlockState iblockstate = worldIn.getBlockState(pos.down());

                if (iblockstate.getMaterial().isSolid())
                {
                    i1 = 0;
                }
                else if (iblockstate.getMaterial() == blockMaterial && iblockstate.getValue(BlockLiquid.LEVEL).intValue() == 0)
                {
                    i1 = 0;
                }
            }

            if (blockMaterial == Material.LAVA && i < 8 && i1 < 8 && i1 > i && rand.nextInt(4) != 0)
            {
                k *= 4;
            }

            if (i1 == i)
            {
                placeStaticBlock(worldIn, pos, state);
            }
            else
            {
                i = i1;

                if (i1 < 0)
                {
                    worldIn.setBlockToAir(pos);
                }
                else
                {
                    state = state.withProperty(BlockLiquid.LEVEL, Integer.valueOf(i1));
                    worldIn.setBlockState(pos, state, 2);
                    worldIn.scheduleUpdate(pos, this, k);
                    worldIn.notifyNeighborsOfStateChange(pos, this, false);
                }
            }
        }
        else
        {
            placeStaticBlock(worldIn, pos, state);
        }

        IBlockState iblockstate1 = worldIn.getBlockState(pos.down());

        if (canFlowInto(worldIn, pos.down(), iblockstate1))
        {
            if (blockMaterial == Material.LAVA && worldIn.getBlockState(pos.down()).getMaterial() == Material.WATER)
            {
                worldIn.setBlockState(pos.down(), Blocks.STONE.getDefaultState());
                triggerMixEffects(worldIn, pos.down());
                return;
            }

            if (i >= 8)
            {
                tryFlowInto(worldIn, pos.down(), iblockstate1, i);
            }
            else
            {
                tryFlowInto(worldIn, pos.down(), iblockstate1, i + 8);
            }
        }
        else if (i >= 0 && (i == 0 || isBlocked(worldIn, pos.down(), iblockstate1)))
        {
            Set<EnumFacing> set = getPossibleFlowDirections(worldIn, pos);
            int k1 = i + j;

            if (i >= 8)
            {
                k1 = 1;
            }

            if (k1 >= 8)
            {
                return;
            }

            for (EnumFacing enumfacing1 : set)
            {
                tryFlowInto(worldIn, pos.offset(enumfacing1), worldIn.getBlockState(pos.offset(enumfacing1)), k1);
            }
        }
    }

    private void tryFlowInto(World worldIn, BlockPos pos, IBlockState state, int level)
    {
        if (canFlowInto(worldIn, pos, state))
        {
            if (state.getMaterial() != Material.AIR)
            {
                if (blockMaterial == Material.LAVA)
                {
                    triggerMixEffects(worldIn, pos);
                }
                else
                {
                    state.getBlock().dropBlockAsItem(worldIn, pos, state, 0);
                }
            }

            worldIn.setBlockState(pos, getDefaultState().withProperty(BlockLiquid.LEVEL, Integer.valueOf(level)), 3);
        }
    }

    private int getSlopeDistance(World worldIn, BlockPos pos, int distance, EnumFacing calculateFlowCost)
    {
        int i = 1000;

        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
        {
            if (enumfacing != calculateFlowCost)
            {
                BlockPos blockpos = pos.offset(enumfacing);
                IBlockState iblockstate = worldIn.getBlockState(blockpos);

                if (!isBlocked(worldIn, blockpos, iblockstate) && (iblockstate.getMaterial() != blockMaterial || iblockstate.getValue(BlockLiquid.LEVEL).intValue() > 0))
                {
                    if (!isBlocked(worldIn, blockpos.down(), iblockstate))
                    {
                        return distance;
                    }

                    if (distance < getSlopeFindDistance(worldIn))
                    {
                        int j = getSlopeDistance(worldIn, blockpos, distance + 1, enumfacing.getOpposite());

                        if (j < i)
                        {
                            i = j;
                        }
                    }
                }
            }
        }

        return i;
    }

    private int getSlopeFindDistance(World worldIn)
    {
        return blockMaterial == Material.LAVA && !worldIn.provider.doesWaterVaporize() ? 2 : 4;
    }

    private Set<EnumFacing> getPossibleFlowDirections(World worldIn, BlockPos pos)
    {
        int i = 1000;
        Set<EnumFacing> set = EnumSet.noneOf(EnumFacing.class);

        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
        {
            BlockPos blockpos = pos.offset(enumfacing);
            IBlockState iblockstate = worldIn.getBlockState(blockpos);

            if (!isBlocked(worldIn, blockpos, iblockstate) && (iblockstate.getMaterial() != blockMaterial || iblockstate.getValue(BlockLiquid.LEVEL).intValue() > 0))
            {
                int j;

                if (isBlocked(worldIn, blockpos.down(), worldIn.getBlockState(blockpos.down())))
                {
                    j = getSlopeDistance(worldIn, blockpos, 1, enumfacing.getOpposite());
                }
                else
                {
                    j = 0;
                }

                if (j < i)
                {
                    set.clear();
                }

                if (j <= i)
                {
                    set.add(enumfacing);
                    i = j;
                }
            }
        }

        return set;
    }

    private boolean isBlocked(World worldIn, BlockPos pos, IBlockState state)
    {
        Block block = worldIn.getBlockState(pos).getBlock();

        if (!(block instanceof BlockDoor) && block != Blocks.STANDING_SIGN && block != Blocks.LADDER && block != Blocks.REEDS)
        {
            return block.blockMaterial == Material.PORTAL || block.blockMaterial == Material.STRUCTURE_VOID || block.blockMaterial.blocksMovement();
        }
        else
        {
            return true;
        }
    }

    protected int checkAdjacentBlock(World worldIn, BlockPos pos, int currentMinLevel)
    {
        int i = getDepth(worldIn.getBlockState(pos));

        if (i < 0)
        {
            return currentMinLevel;
        }
        else
        {
            if (i == 0)
            {
                ++adjacentSourceBlocks;
            }

            if (i >= 8)
            {
                i = 0;
            }

            return currentMinLevel >= 0 && i >= currentMinLevel ? currentMinLevel : i;
        }
    }

    private boolean canFlowInto(World worldIn, BlockPos pos, IBlockState state)
    {
        Material material = state.getMaterial();
        return material != blockMaterial && material != Material.LAVA && !isBlocked(worldIn, pos, state);
    }

    /**
     * Called after the block is set in the Chunk data, but before the Tile Entity is set
     */
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!checkForMixing(worldIn, pos, state))
        {
            worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
        }
    }
}
