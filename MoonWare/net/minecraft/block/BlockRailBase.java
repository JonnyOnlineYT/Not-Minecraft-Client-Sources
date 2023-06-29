package net.minecraft.block;

import com.google.common.collect.Lists;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockRailBase extends Block
{
    protected static final AxisAlignedBB FLAT_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
    protected static final AxisAlignedBB field_190959_b = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected final boolean isPowered;

    public static boolean isRailBlock(World worldIn, BlockPos pos)
    {
        return isRailBlock(worldIn.getBlockState(pos));
    }

    public static boolean isRailBlock(IBlockState state)
    {
        Block block = state.getBlock();
        return block == Blocks.RAIL || block == Blocks.GOLDEN_RAIL || block == Blocks.DETECTOR_RAIL || block == Blocks.ACTIVATOR_RAIL;
    }

    protected BlockRailBase(boolean isPowered)
    {
        super(Material.CIRCUITS);
        this.isPowered = isPowered;
        setCreativeTab(CreativeTabs.TRANSPORTATION);
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return Block.NULL_AABB;
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = state.getBlock() == this ? state.getValue(getShapeProperty()) : null;
        return blockrailbase$enumraildirection != null && blockrailbase$enumraildirection.isAscending() ? field_190959_b : FLAT_AABB;
    }

    public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    {
        return BlockFaceShape.UNDEFINED;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos.down()).isFullyOpaque();
    }

    /**
     * Called after the block is set in the Chunk data, but before the Tile Entity is set
     */
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote)
        {
            state = updateDir(worldIn, pos, state, true);

            if (isPowered)
            {
                state.neighborChanged(worldIn, pos, this, pos);
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
        if (!worldIn.isRemote)
        {
            BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = state.getValue(getShapeProperty());
            boolean flag = !worldIn.getBlockState(pos.down()).isFullyOpaque();

            if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.ASCENDING_EAST && !worldIn.getBlockState(pos.east()).isFullyOpaque())
            {
                flag = true;
            }
            else if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.ASCENDING_WEST && !worldIn.getBlockState(pos.west()).isFullyOpaque())
            {
                flag = true;
            }
            else if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.ASCENDING_NORTH && !worldIn.getBlockState(pos.north()).isFullyOpaque())
            {
                flag = true;
            }
            else if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.ASCENDING_SOUTH && !worldIn.getBlockState(pos.south()).isFullyOpaque())
            {
                flag = true;
            }

            if (flag && !worldIn.isAirBlock(pos))
            {
                dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
            else
            {
                updateState(state, worldIn, pos, blockIn);
            }
        }
    }

    protected void updateState(IBlockState p_189541_1_, World p_189541_2_, BlockPos p_189541_3_, Block p_189541_4_)
    {
    }

    protected IBlockState updateDir(World worldIn, BlockPos pos, IBlockState state, boolean p_176564_4_)
    {
        return worldIn.isRemote ? state : (new BlockRailBase.Rail(worldIn, pos, state)).place(worldIn.isBlockPowered(pos), p_176564_4_).getBlockState();
    }

    public EnumPushReaction getMobilityFlag(IBlockState state)
    {
        return EnumPushReaction.NORMAL;
    }

    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    /**
     * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
     */
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);

        if (state.getValue(getShapeProperty()).isAscending())
        {
            worldIn.notifyNeighborsOfStateChange(pos.up(), this, false);
        }

        if (isPowered)
        {
            worldIn.notifyNeighborsOfStateChange(pos, this, false);
            worldIn.notifyNeighborsOfStateChange(pos.down(), this, false);
        }
    }

    public abstract IProperty<BlockRailBase.EnumRailDirection> getShapeProperty();

    public enum EnumRailDirection implements IStringSerializable
    {
        NORTH_SOUTH(0, "north_south"),
        EAST_WEST(1, "east_west"),
        ASCENDING_EAST(2, "ascending_east"),
        ASCENDING_WEST(3, "ascending_west"),
        ASCENDING_NORTH(4, "ascending_north"),
        ASCENDING_SOUTH(5, "ascending_south"),
        SOUTH_EAST(6, "south_east"),
        SOUTH_WEST(7, "south_west"),
        NORTH_WEST(8, "north_west"),
        NORTH_EAST(9, "north_east");

        private static final BlockRailBase.EnumRailDirection[] META_LOOKUP = new BlockRailBase.EnumRailDirection[values().length];
        private final int meta;
        private final String name;

        EnumRailDirection(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMetadata()
        {
            return meta;
        }

        public String toString()
        {
            return name;
        }

        public boolean isAscending()
        {
            return this == ASCENDING_NORTH || this == ASCENDING_EAST || this == ASCENDING_SOUTH || this == ASCENDING_WEST;
        }

        public static BlockRailBase.EnumRailDirection byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public String getName()
        {
            return name;
        }

        static {
            for (BlockRailBase.EnumRailDirection blockrailbase$enumraildirection : values())
            {
                META_LOOKUP[blockrailbase$enumraildirection.getMetadata()] = blockrailbase$enumraildirection;
            }
        }
    }

    public class Rail
    {
        private final World world;
        private final BlockPos pos;
        private final BlockRailBase block;
        private IBlockState state;
        private final boolean isPowered;
        private final List<BlockPos> connectedRails = Lists.newArrayList();

        public Rail(World worldIn, BlockPos pos, IBlockState state)
        {
            world = worldIn;
            this.pos = pos;
            this.state = state;
            block = (BlockRailBase)state.getBlock();
            BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = state.getValue(block.getShapeProperty());
            isPowered = block.isPowered;
            updateConnectedRails(blockrailbase$enumraildirection);
        }

        public List<BlockPos> getConnectedRails()
        {
            return connectedRails;
        }

        private void updateConnectedRails(BlockRailBase.EnumRailDirection railDirection)
        {
            connectedRails.clear();

            switch (railDirection)
            {
                case NORTH_SOUTH:
                    connectedRails.add(pos.north());
                    connectedRails.add(pos.south());
                    break;

                case EAST_WEST:
                    connectedRails.add(pos.west());
                    connectedRails.add(pos.east());
                    break;

                case ASCENDING_EAST:
                    connectedRails.add(pos.west());
                    connectedRails.add(pos.east().up());
                    break;

                case ASCENDING_WEST:
                    connectedRails.add(pos.west().up());
                    connectedRails.add(pos.east());
                    break;

                case ASCENDING_NORTH:
                    connectedRails.add(pos.north().up());
                    connectedRails.add(pos.south());
                    break;

                case ASCENDING_SOUTH:
                    connectedRails.add(pos.north());
                    connectedRails.add(pos.south().up());
                    break;

                case SOUTH_EAST:
                    connectedRails.add(pos.east());
                    connectedRails.add(pos.south());
                    break;

                case SOUTH_WEST:
                    connectedRails.add(pos.west());
                    connectedRails.add(pos.south());
                    break;

                case NORTH_WEST:
                    connectedRails.add(pos.west());
                    connectedRails.add(pos.north());
                    break;

                case NORTH_EAST:
                    connectedRails.add(pos.east());
                    connectedRails.add(pos.north());
            }
        }

        private void removeSoftConnections()
        {
            for (int i = 0; i < connectedRails.size(); ++i)
            {
                BlockRailBase.Rail blockrailbase$rail = findRailAt(connectedRails.get(i));

                if (blockrailbase$rail != null && blockrailbase$rail.isConnectedToRail(this))
                {
                    connectedRails.set(i, blockrailbase$rail.pos);
                }
                else
                {
                    connectedRails.remove(i--);
                }
            }
        }

        private boolean hasRailAt(BlockPos pos)
        {
            return isRailBlock(world, pos) || isRailBlock(world, pos.up()) || isRailBlock(world, pos.down());
        }

        @Nullable
        private BlockRailBase.Rail findRailAt(BlockPos pos)
        {
            IBlockState iblockstate = world.getBlockState(pos);

            if (isRailBlock(iblockstate))
            {
                return BlockRailBase.this.new Rail(world, pos, iblockstate);
            }
            else
            {
                BlockPos lvt_2_1_ = pos.up();
                iblockstate = world.getBlockState(lvt_2_1_);

                if (isRailBlock(iblockstate))
                {
                    return BlockRailBase.this.new Rail(world, lvt_2_1_, iblockstate);
                }
                else
                {
                    lvt_2_1_ = pos.down();
                    iblockstate = world.getBlockState(lvt_2_1_);
                    return isRailBlock(iblockstate) ? BlockRailBase.this.new Rail(world, lvt_2_1_, iblockstate) : null;
                }
            }
        }

        private boolean isConnectedToRail(BlockRailBase.Rail rail)
        {
            return isConnectedTo(rail.pos);
        }

        private boolean isConnectedTo(BlockPos posIn)
        {
            for (int i = 0; i < connectedRails.size(); ++i)
            {
                BlockPos blockpos = connectedRails.get(i);

                if (blockpos.getX() == posIn.getX() && blockpos.getZ() == posIn.getZ())
                {
                    return true;
                }
            }

            return false;
        }

        protected int countAdjacentRails()
        {
            int i = 0;

            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
            {
                if (hasRailAt(pos.offset(enumfacing)))
                {
                    ++i;
                }
            }

            return i;
        }

        private boolean canConnectTo(BlockRailBase.Rail rail)
        {
            return isConnectedToRail(rail) || connectedRails.size() != 2;
        }

        private void connectTo(BlockRailBase.Rail p_150645_1_)
        {
            connectedRails.add(p_150645_1_.pos);
            BlockPos blockpos = pos.north();
            BlockPos blockpos1 = pos.south();
            BlockPos blockpos2 = pos.west();
            BlockPos blockpos3 = pos.east();
            boolean flag = isConnectedTo(blockpos);
            boolean flag1 = isConnectedTo(blockpos1);
            boolean flag2 = isConnectedTo(blockpos2);
            boolean flag3 = isConnectedTo(blockpos3);
            BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = null;

            if (flag || flag1)
            {
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }

            if (flag2 || flag3)
            {
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.EAST_WEST;
            }

            if (!isPowered)
            {
                if (flag1 && flag3 && !flag && !flag2)
                {
                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_EAST;
                }

                if (flag1 && flag2 && !flag && !flag3)
                {
                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_WEST;
                }

                if (flag && flag2 && !flag1 && !flag3)
                {
                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_WEST;
                }

                if (flag && flag3 && !flag1 && !flag2)
                {
                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_EAST;
                }
            }

            if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.NORTH_SOUTH)
            {
                if (isRailBlock(world, blockpos.up()))
                {
                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_NORTH;
                }

                if (isRailBlock(world, blockpos1.up()))
                {
                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_SOUTH;
                }
            }

            if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.EAST_WEST)
            {
                if (isRailBlock(world, blockpos3.up()))
                {
                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_EAST;
                }

                if (isRailBlock(world, blockpos2.up()))
                {
                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_WEST;
                }
            }

            if (blockrailbase$enumraildirection == null)
            {
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }

            state = state.withProperty(block.getShapeProperty(), blockrailbase$enumraildirection);
            world.setBlockState(pos, state, 3);
        }

        private boolean hasNeighborRail(BlockPos p_180361_1_)
        {
            BlockRailBase.Rail blockrailbase$rail = findRailAt(p_180361_1_);

            if (blockrailbase$rail == null)
            {
                return false;
            }
            else
            {
                blockrailbase$rail.removeSoftConnections();
                return blockrailbase$rail.canConnectTo(this);
            }
        }

        public BlockRailBase.Rail place(boolean p_180364_1_, boolean p_180364_2_)
        {
            BlockPos blockpos = pos.north();
            BlockPos blockpos1 = pos.south();
            BlockPos blockpos2 = pos.west();
            BlockPos blockpos3 = pos.east();
            boolean flag = hasNeighborRail(blockpos);
            boolean flag1 = hasNeighborRail(blockpos1);
            boolean flag2 = hasNeighborRail(blockpos2);
            boolean flag3 = hasNeighborRail(blockpos3);
            BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = null;

            if ((flag || flag1) && !flag2 && !flag3)
            {
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }

            if ((flag2 || flag3) && !flag && !flag1)
            {
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.EAST_WEST;
            }

            if (!isPowered)
            {
                if (flag1 && flag3 && !flag && !flag2)
                {
                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_EAST;
                }

                if (flag1 && flag2 && !flag && !flag3)
                {
                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_WEST;
                }

                if (flag && flag2 && !flag1 && !flag3)
                {
                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_WEST;
                }

                if (flag && flag3 && !flag1 && !flag2)
                {
                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_EAST;
                }
            }

            if (blockrailbase$enumraildirection == null)
            {
                if (flag || flag1)
                {
                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
                }

                if (flag2 || flag3)
                {
                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.EAST_WEST;
                }

                if (!isPowered)
                {
                    if (p_180364_1_)
                    {
                        if (flag1 && flag3)
                        {
                            blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_EAST;
                        }

                        if (flag2 && flag1)
                        {
                            blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_WEST;
                        }

                        if (flag3 && flag)
                        {
                            blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_EAST;
                        }

                        if (flag && flag2)
                        {
                            blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_WEST;
                        }
                    }
                    else
                    {
                        if (flag && flag2)
                        {
                            blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_WEST;
                        }

                        if (flag3 && flag)
                        {
                            blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_EAST;
                        }

                        if (flag2 && flag1)
                        {
                            blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_WEST;
                        }

                        if (flag1 && flag3)
                        {
                            blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_EAST;
                        }
                    }
                }
            }

            if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.NORTH_SOUTH)
            {
                if (isRailBlock(world, blockpos.up()))
                {
                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_NORTH;
                }

                if (isRailBlock(world, blockpos1.up()))
                {
                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_SOUTH;
                }
            }

            if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.EAST_WEST)
            {
                if (isRailBlock(world, blockpos3.up()))
                {
                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_EAST;
                }

                if (isRailBlock(world, blockpos2.up()))
                {
                    blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_WEST;
                }
            }

            if (blockrailbase$enumraildirection == null)
            {
                blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }

            updateConnectedRails(blockrailbase$enumraildirection);
            state = state.withProperty(block.getShapeProperty(), blockrailbase$enumraildirection);

            if (p_180364_2_ || world.getBlockState(pos) != state)
            {
                world.setBlockState(pos, state, 3);

                for (int i = 0; i < connectedRails.size(); ++i)
                {
                    BlockRailBase.Rail blockrailbase$rail = findRailAt(connectedRails.get(i));

                    if (blockrailbase$rail != null)
                    {
                        blockrailbase$rail.removeSoftConnections();

                        if (blockrailbase$rail.canConnectTo(this))
                        {
                            blockrailbase$rail.connectTo(this);
                        }
                    }
                }
            }

            return this;
        }

        public IBlockState getBlockState()
        {
            return state;
        }
    }
}
