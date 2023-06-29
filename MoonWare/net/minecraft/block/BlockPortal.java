package net.minecraft.block;

import com.google.common.cache.LoadingCache;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockPortal extends BlockBreakable
{
    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class, EnumFacing.Axis.X, EnumFacing.Axis.Z);
    protected static final AxisAlignedBB X_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D);
    protected static final AxisAlignedBB Z_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D);
    protected static final AxisAlignedBB Y_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D);

    public BlockPortal()
    {
        super(Material.PORTAL, false);
        setDefaultState(blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.X));
        setTickRandomly(true);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        switch (state.getValue(AXIS))
        {
            case X:
                return X_AABB;

            case Y:
            default:
                return Y_AABB;

            case Z:
                return Z_AABB;
        }
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        super.updateTick(worldIn, pos, state, rand);

        if (worldIn.provider.isSurfaceWorld() && worldIn.getGameRules().getBoolean("doMobSpawning") && rand.nextInt(2000) < worldIn.getDifficulty().getDifficultyId())
        {
            int i = pos.getY();
            BlockPos blockpos;

            for (blockpos = pos; !worldIn.getBlockState(blockpos).isFullyOpaque() && blockpos.getY() > 0; blockpos = blockpos.down())
            {
            }

            if (i > 0 && !worldIn.getBlockState(blockpos.up()).isNormalCube())
            {
                Entity entity = ItemMonsterPlacer.spawnCreature(worldIn, EntityList.getKey(EntityPigZombie.class), (double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 1.1D, (double)blockpos.getZ() + 0.5D);

                if (entity != null)
                {
                    entity.timeUntilPortal = entity.getPortalCooldown();
                }
            }
        }
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return Block.NULL_AABB;
    }

    public static int getMetaForAxis(EnumFacing.Axis axis)
    {
        if (axis == EnumFacing.Axis.X)
        {
            return 1;
        }
        else
        {
            return axis == EnumFacing.Axis.Z ? 2 : 0;
        }
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    public boolean trySpawnPortal(World worldIn, BlockPos pos)
    {
        BlockPortal.Size blockportal$size = new BlockPortal.Size(worldIn, pos, EnumFacing.Axis.X);

        if (blockportal$size.isValid() && blockportal$size.portalBlockCount == 0)
        {
            blockportal$size.placePortalBlocks();
            return true;
        }
        else
        {
            BlockPortal.Size blockportal$size1 = new BlockPortal.Size(worldIn, pos, EnumFacing.Axis.Z);

            if (blockportal$size1.isValid() && blockportal$size1.portalBlockCount == 0)
            {
                blockportal$size1.placePortalBlocks();
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    /**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     */
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_)
    {
        EnumFacing.Axis enumfacing$axis = state.getValue(AXIS);

        if (enumfacing$axis == EnumFacing.Axis.X)
        {
            BlockPortal.Size blockportal$size = new BlockPortal.Size(worldIn, pos, EnumFacing.Axis.X);

            if (!blockportal$size.isValid() || blockportal$size.portalBlockCount < blockportal$size.width * blockportal$size.height)
            {
                worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
            }
        }
        else if (enumfacing$axis == EnumFacing.Axis.Z)
        {
            BlockPortal.Size blockportal$size1 = new BlockPortal.Size(worldIn, pos, EnumFacing.Axis.Z);

            if (!blockportal$size1.isValid() || blockportal$size1.portalBlockCount < blockportal$size1.width * blockportal$size1.height)
            {
                worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
            }
        }
    }

    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        pos = pos.offset(side);
        EnumFacing.Axis enumfacing$axis = null;

        if (blockState.getBlock() == this)
        {
            enumfacing$axis = blockState.getValue(AXIS);

            if (enumfacing$axis == null)
            {
                return false;
            }

            if (enumfacing$axis == EnumFacing.Axis.Z && side != EnumFacing.EAST && side != EnumFacing.WEST)
            {
                return false;
            }

            if (enumfacing$axis == EnumFacing.Axis.X && side != EnumFacing.SOUTH && side != EnumFacing.NORTH)
            {
                return false;
            }
        }

        boolean flag = blockAccess.getBlockState(pos.west()).getBlock() == this && blockAccess.getBlockState(pos.west(2)).getBlock() != this;
        boolean flag1 = blockAccess.getBlockState(pos.east()).getBlock() == this && blockAccess.getBlockState(pos.east(2)).getBlock() != this;
        boolean flag2 = blockAccess.getBlockState(pos.north()).getBlock() == this && blockAccess.getBlockState(pos.north(2)).getBlock() != this;
        boolean flag3 = blockAccess.getBlockState(pos.south()).getBlock() == this && blockAccess.getBlockState(pos.south(2)).getBlock() != this;
        boolean flag4 = flag || flag1 || enumfacing$axis == EnumFacing.Axis.X;
        boolean flag5 = flag2 || flag3 || enumfacing$axis == EnumFacing.Axis.Z;

        if (flag4 && side == EnumFacing.WEST)
        {
            return true;
        }
        else if (flag4 && side == EnumFacing.EAST)
        {
            return true;
        }
        else if (flag5 && side == EnumFacing.NORTH)
        {
            return true;
        }
        else
        {
            return flag5 && side == EnumFacing.SOUTH;
        }
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random)
    {
        return 0;
    }

    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }

    /**
     * Called When an Entity Collided with the Block
     */
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if (!entityIn.isRiding() && !entityIn.isBeingRidden() && entityIn.isNonBoss())
        {
            entityIn.setPortal(pos);
        }
    }

    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (rand.nextInt(100) == 0)
        {
            worldIn.playSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.BLOCKS, 0.5F, rand.nextFloat() * 0.4F + 0.8F, false);
        }

        for (int i = 0; i < 4; ++i)
        {
            double d0 = (float)pos.getX() + rand.nextFloat();
            double d1 = (float)pos.getY() + rand.nextFloat();
            double d2 = (float)pos.getZ() + rand.nextFloat();
            double d3 = ((double)rand.nextFloat() - 0.5D) * 0.5D;
            double d4 = ((double)rand.nextFloat() - 0.5D) * 0.5D;
            double d5 = ((double)rand.nextFloat() - 0.5D) * 0.5D;
            int j = rand.nextInt(2) * 2 - 1;

            if (worldIn.getBlockState(pos.west()).getBlock() != this && worldIn.getBlockState(pos.east()).getBlock() != this)
            {
                d0 = (double)pos.getX() + 0.5D + 0.25D * (double)j;
                d3 = rand.nextFloat() * 2.0F * (float)j;
            }
            else
            {
                d2 = (double)pos.getZ() + 0.5D + 0.25D * (double)j;
                d5 = rand.nextFloat() * 2.0F * (float)j;
            }

            worldIn.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
        }
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return ItemStack.EMPTY;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(AXIS, (meta & 3) == 2 ? EnumFacing.Axis.Z : EnumFacing.Axis.X);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return getMetaForAxis(state.getValue(AXIS));
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        switch (rot)
        {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch (state.getValue(AXIS))
                {
                    case X:
                        return state.withProperty(AXIS, EnumFacing.Axis.Z);

                    case Z:
                        return state.withProperty(AXIS, EnumFacing.Axis.X);

                    default:
                        return state;
                }

            default:
                return state;
        }
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, AXIS);
    }

    public BlockPattern.PatternHelper createPatternHelper(World worldIn, BlockPos p_181089_2_)
    {
        EnumFacing.Axis enumfacing$axis = EnumFacing.Axis.Z;
        BlockPortal.Size blockportal$size = new BlockPortal.Size(worldIn, p_181089_2_, EnumFacing.Axis.X);
        LoadingCache<BlockPos, BlockWorldState> loadingcache = BlockPattern.createLoadingCache(worldIn, true);

        if (!blockportal$size.isValid())
        {
            enumfacing$axis = EnumFacing.Axis.X;
            blockportal$size = new BlockPortal.Size(worldIn, p_181089_2_, EnumFacing.Axis.Z);
        }

        if (!blockportal$size.isValid())
        {
            return new BlockPattern.PatternHelper(p_181089_2_, EnumFacing.NORTH, EnumFacing.UP, loadingcache, 1, 1, 1);
        }
        else
        {
            int[] aint = new int[EnumFacing.AxisDirection.values().length];
            EnumFacing enumfacing = blockportal$size.rightDir.rotateYCCW();
            BlockPos blockpos = blockportal$size.bottomLeft.up(blockportal$size.getHeight() - 1);

            for (EnumFacing.AxisDirection enumfacing$axisdirection : EnumFacing.AxisDirection.values())
            {
                BlockPattern.PatternHelper blockpattern$patternhelper = new BlockPattern.PatternHelper(enumfacing.getAxisDirection() == enumfacing$axisdirection ? blockpos : blockpos.offset(blockportal$size.rightDir, blockportal$size.getWidth() - 1), EnumFacing.getFacingFromAxis(enumfacing$axisdirection, enumfacing$axis), EnumFacing.UP, loadingcache, blockportal$size.getWidth(), blockportal$size.getHeight(), 1);

                for (int i = 0; i < blockportal$size.getWidth(); ++i)
                {
                    for (int j = 0; j < blockportal$size.getHeight(); ++j)
                    {
                        BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(i, j, 1);

                        if (blockworldstate.getBlockState() != null && blockworldstate.getBlockState().getMaterial() != Material.AIR)
                        {
                            ++aint[enumfacing$axisdirection.ordinal()];
                        }
                    }
                }
            }

            EnumFacing.AxisDirection enumfacing$axisdirection1 = EnumFacing.AxisDirection.POSITIVE;

            for (EnumFacing.AxisDirection enumfacing$axisdirection2 : EnumFacing.AxisDirection.values())
            {
                if (aint[enumfacing$axisdirection2.ordinal()] < aint[enumfacing$axisdirection1.ordinal()])
                {
                    enumfacing$axisdirection1 = enumfacing$axisdirection2;
                }
            }

            return new BlockPattern.PatternHelper(enumfacing.getAxisDirection() == enumfacing$axisdirection1 ? blockpos : blockpos.offset(blockportal$size.rightDir, blockportal$size.getWidth() - 1), EnumFacing.getFacingFromAxis(enumfacing$axisdirection1, enumfacing$axis), EnumFacing.UP, loadingcache, blockportal$size.getWidth(), blockportal$size.getHeight(), 1);
        }
    }

    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        return BlockFaceShape.UNDEFINED;
    }

    public static class Size
    {
        private final World world;
        private final EnumFacing.Axis axis;
        private final EnumFacing rightDir;
        private final EnumFacing leftDir;
        private int portalBlockCount;
        private BlockPos bottomLeft;
        private int height;
        private int width;

        public Size(World worldIn, BlockPos p_i45694_2_, EnumFacing.Axis p_i45694_3_)
        {
            world = worldIn;
            axis = p_i45694_3_;

            if (p_i45694_3_ == EnumFacing.Axis.X)
            {
                leftDir = EnumFacing.EAST;
                rightDir = EnumFacing.WEST;
            }
            else
            {
                leftDir = EnumFacing.NORTH;
                rightDir = EnumFacing.SOUTH;
            }

            for (BlockPos blockpos = p_i45694_2_; p_i45694_2_.getY() > blockpos.getY() - 21 && p_i45694_2_.getY() > 0 && isEmptyBlock(worldIn.getBlockState(p_i45694_2_.down()).getBlock()); p_i45694_2_ = p_i45694_2_.down())
            {
            }

            int i = getDistanceUntilEdge(p_i45694_2_, leftDir) - 1;

            if (i >= 0)
            {
                bottomLeft = p_i45694_2_.offset(leftDir, i);
                width = getDistanceUntilEdge(bottomLeft, rightDir);

                if (width < 2 || width > 21)
                {
                    bottomLeft = null;
                    width = 0;
                }
            }

            if (bottomLeft != null)
            {
                height = calculatePortalHeight();
            }
        }

        protected int getDistanceUntilEdge(BlockPos p_180120_1_, EnumFacing p_180120_2_)
        {
            int i;

            for (i = 0; i < 22; ++i)
            {
                BlockPos blockpos = p_180120_1_.offset(p_180120_2_, i);

                if (!isEmptyBlock(world.getBlockState(blockpos).getBlock()) || world.getBlockState(blockpos.down()).getBlock() != Blocks.OBSIDIAN)
                {
                    break;
                }
            }

            Block block = world.getBlockState(p_180120_1_.offset(p_180120_2_, i)).getBlock();
            return block == Blocks.OBSIDIAN ? i : 0;
        }

        public int getHeight()
        {
            return height;
        }

        public int getWidth()
        {
            return width;
        }

        protected int calculatePortalHeight()
        {
            label56:

            for (height = 0; height < 21; ++height)
            {
                for (int i = 0; i < width; ++i)
                {
                    BlockPos blockpos = bottomLeft.offset(rightDir, i).up(height);
                    Block block = world.getBlockState(blockpos).getBlock();

                    if (!isEmptyBlock(block))
                    {
                        break label56;
                    }

                    if (block == Blocks.PORTAL)
                    {
                        ++portalBlockCount;
                    }

                    if (i == 0)
                    {
                        block = world.getBlockState(blockpos.offset(leftDir)).getBlock();

                        if (block != Blocks.OBSIDIAN)
                        {
                            break label56;
                        }
                    }
                    else if (i == width - 1)
                    {
                        block = world.getBlockState(blockpos.offset(rightDir)).getBlock();

                        if (block != Blocks.OBSIDIAN)
                        {
                            break label56;
                        }
                    }
                }
            }

            for (int j = 0; j < width; ++j)
            {
                if (world.getBlockState(bottomLeft.offset(rightDir, j).up(height)).getBlock() != Blocks.OBSIDIAN)
                {
                    height = 0;
                    break;
                }
            }

            if (height <= 21 && height >= 3)
            {
                return height;
            }
            else
            {
                bottomLeft = null;
                width = 0;
                height = 0;
                return 0;
            }
        }

        protected boolean isEmptyBlock(Block blockIn)
        {
            return blockIn.blockMaterial == Material.AIR || blockIn == Blocks.FIRE || blockIn == Blocks.PORTAL;
        }

        public boolean isValid()
        {
            return bottomLeft != null && width >= 2 && width <= 21 && height >= 3 && height <= 21;
        }

        public void placePortalBlocks()
        {
            for (int i = 0; i < width; ++i)
            {
                BlockPos blockpos = bottomLeft.offset(rightDir, i);

                for (int j = 0; j < height; ++j)
                {
                    world.setBlockState(blockpos.up(j), Blocks.PORTAL.getDefaultState().withProperty(AXIS, axis), 2);
                }
            }
        }
    }
}
