package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockPistonStructureHelper;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPistonBase extends BlockDirectional {
   public static final PropertyBool EXTENDED = PropertyBool.create("extended");
   protected static final AxisAlignedBB PISTON_BASE_EAST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 1.0D);
   protected static final AxisAlignedBB PISTON_BASE_WEST_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
   protected static final AxisAlignedBB PISTON_BASE_SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D);
   protected static final AxisAlignedBB PISTON_BASE_NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D);
   protected static final AxisAlignedBB PISTON_BASE_UP_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);
   protected static final AxisAlignedBB PISTON_BASE_DOWN_AABB = new AxisAlignedBB(0.0D, 0.25D, 0.0D, 1.0D, 1.0D, 1.0D);
   private final boolean isSticky;

   public BlockPistonBase(boolean isSticky) {
      super(Material.PISTON);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(EXTENDED, false));
      this.isSticky = isSticky;
      this.setSoundType(SoundType.STONE);
      this.setHardness(0.5F);
      this.setCreativeTab(CreativeTabs.REDSTONE);
   }

   public boolean causesSuffocation(IBlockState p_176214_1_) {
      return !(Boolean)p_176214_1_.getValue(EXTENDED);
   }

   public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
      if ((Boolean)state.getValue(EXTENDED)) {
         switch((EnumFacing)state.getValue(FACING)) {
         case DOWN:
            return PISTON_BASE_DOWN_AABB;
         case UP:
         default:
            return PISTON_BASE_UP_AABB;
         case NORTH:
            return PISTON_BASE_NORTH_AABB;
         case SOUTH:
            return PISTON_BASE_SOUTH_AABB;
         case WEST:
            return PISTON_BASE_WEST_AABB;
         case EAST:
            return PISTON_BASE_EAST_AABB;
         }
      } else {
         return FULL_BLOCK_AABB;
      }
   }

   public boolean isFullyOpaque(IBlockState state) {
      return !(Boolean)state.getValue(EXTENDED) || state.getValue(FACING) == EnumFacing.DOWN;
   }

   public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
      addCollisionBoxToList(pos, entityBox, collidingBoxes, state.getBoundingBox(worldIn, pos));
   }

   public boolean isOpaqueCube(IBlockState state) {
      return false;
   }

   public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
      worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing.func_190914_a(pos, placer)), 2);
      if (!worldIn.isRemote) {
         this.checkForMove(worldIn, pos, state);
      }

   }

   public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
      if (!worldIn.isRemote) {
         this.checkForMove(worldIn, pos, state);
      }

   }

   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
      if (!worldIn.isRemote && worldIn.getTileEntity(pos) == null) {
         this.checkForMove(worldIn, pos, state);
      }

   }

   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      return this.getDefaultState().withProperty(FACING, EnumFacing.func_190914_a(pos, placer)).withProperty(EXTENDED, false);
   }

   private void checkForMove(World worldIn, BlockPos pos, IBlockState state) {
      EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
      boolean flag = this.shouldBeExtended(worldIn, pos, enumfacing);
      if (flag && !(Boolean)state.getValue(EXTENDED)) {
         if ((new BlockPistonStructureHelper(worldIn, pos, enumfacing, true)).canMove()) {
            worldIn.addBlockEvent(pos, this, 0, enumfacing.getIndex());
         }
      } else if (!flag && (Boolean)state.getValue(EXTENDED)) {
         worldIn.addBlockEvent(pos, this, 1, enumfacing.getIndex());
      }

   }

   private boolean shouldBeExtended(World worldIn, BlockPos pos, EnumFacing facing) {
      EnumFacing[] var4 = EnumFacing.values();
      int var5 = var4.length;

      int var6;
      for(var6 = 0; var6 < var5; ++var6) {
         EnumFacing enumfacing = var4[var6];
         if (enumfacing != facing && worldIn.isSidePowered(pos.offset(enumfacing), enumfacing)) {
            return true;
         }
      }

      if (worldIn.isSidePowered(pos, EnumFacing.DOWN)) {
         return true;
      } else {
         BlockPos blockpos = pos.up();
         EnumFacing[] var10 = EnumFacing.values();
         var6 = var10.length;

         for(int var11 = 0; var11 < var6; ++var11) {
            EnumFacing enumfacing1 = var10[var11];
            if (enumfacing1 != EnumFacing.DOWN && worldIn.isSidePowered(blockpos.offset(enumfacing1), enumfacing1)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
      EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
      if (!worldIn.isRemote) {
         boolean flag = this.shouldBeExtended(worldIn, pos, enumfacing);
         if (flag && id == 1) {
            worldIn.setBlockState(pos, state.withProperty(EXTENDED, true), 2);
            return false;
         }

         if (!flag && id == 0) {
            return false;
         }
      }

      if (id == 0) {
         if (!this.doMove(worldIn, pos, enumfacing, true)) {
            return false;
         }

         worldIn.setBlockState(pos, state.withProperty(EXTENDED, true), 3);
         worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, worldIn.rand.nextFloat() * 0.25F + 0.6F);
      } else if (id == 1) {
         TileEntity tileentity1 = worldIn.getTileEntity(pos.offset(enumfacing));
         if (tileentity1 instanceof TileEntityPiston) {
            ((TileEntityPiston)tileentity1).clearPistonTileEntity();
         }

         worldIn.setBlockState(pos, Blocks.PISTON_EXTENSION.getDefaultState().withProperty(BlockPistonMoving.FACING, enumfacing).withProperty(BlockPistonMoving.TYPE, this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT), 3);
         worldIn.setTileEntity(pos, BlockPistonMoving.createTilePiston(this.getStateFromMeta(param), enumfacing, false, true));
         if (this.isSticky) {
            BlockPos blockpos = pos.add(enumfacing.getFrontOffsetX() * 2, enumfacing.getFrontOffsetY() * 2, enumfacing.getFrontOffsetZ() * 2);
            IBlockState iblockstate = worldIn.getBlockState(blockpos);
            Block block = iblockstate.getBlock();
            boolean flag1 = false;
            if (block == Blocks.PISTON_EXTENSION) {
               TileEntity tileentity = worldIn.getTileEntity(blockpos);
               if (tileentity instanceof TileEntityPiston) {
                  TileEntityPiston tileentitypiston = (TileEntityPiston)tileentity;
                  if (tileentitypiston.getFacing() == enumfacing && tileentitypiston.isExtending()) {
                     tileentitypiston.clearPistonTileEntity();
                     flag1 = true;
                  }
               }
            }

            if (!flag1 && iblockstate.getMaterial() != Material.AIR && canPush(iblockstate, worldIn, blockpos, enumfacing.getOpposite(), false, enumfacing) && (iblockstate.getMobilityFlag() == EnumPushReaction.NORMAL || block == Blocks.PISTON || block == Blocks.STICKY_PISTON)) {
               this.doMove(worldIn, pos, enumfacing, false);
            }
         } else {
            worldIn.setBlockToAir(pos.offset(enumfacing));
         }

         worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, worldIn.rand.nextFloat() * 0.15F + 0.6F);
      }

      return true;
   }

   public boolean isFullCube(IBlockState state) {
      return false;
   }

   @Nullable
   public static EnumFacing getFacing(int meta) {
      int i = meta & 7;
      return i > 5 ? null : EnumFacing.getFront(i);
   }

   public static boolean canPush(IBlockState blockStateIn, World worldIn, BlockPos pos, EnumFacing facing, boolean destroyBlocks, EnumFacing p_185646_5_) {
      Block block = blockStateIn.getBlock();
      if (block == Blocks.OBSIDIAN) {
         return false;
      } else if (!worldIn.getWorldBorder().contains(pos)) {
         return false;
      } else if (pos.getY() < 0 || facing == EnumFacing.DOWN && pos.getY() == 0) {
         return false;
      } else if (pos.getY() > worldIn.getHeight() - 1 || facing == EnumFacing.UP && pos.getY() == worldIn.getHeight() - 1) {
         return false;
      } else {
         if (block != Blocks.PISTON && block != Blocks.STICKY_PISTON) {
            if (blockStateIn.getBlockHardness(worldIn, pos) == -1.0F) {
               return false;
            }

            switch(blockStateIn.getMobilityFlag()) {
            case BLOCK:
               return false;
            case DESTROY:
               return destroyBlocks;
            case PUSH_ONLY:
               return facing == p_185646_5_;
            }
         } else if ((Boolean)blockStateIn.getValue(EXTENDED)) {
            return false;
         }

         return !block.hasTileEntity();
      }
   }

   private boolean doMove(World worldIn, BlockPos pos, EnumFacing direction, boolean extending) {
      if (!extending) {
         worldIn.setBlockToAir(pos.offset(direction));
      }

      BlockPistonStructureHelper blockpistonstructurehelper = new BlockPistonStructureHelper(worldIn, pos, direction, extending);
      if (!blockpistonstructurehelper.canMove()) {
         return false;
      } else {
         List<BlockPos> list = blockpistonstructurehelper.getBlocksToMove();
         List<IBlockState> list1 = Lists.newArrayList();

         for(int i = 0; i < list.size(); ++i) {
            BlockPos blockpos = (BlockPos)list.get(i);
            list1.add(worldIn.getBlockState(blockpos).getActualState(worldIn, blockpos));
         }

         List<BlockPos> list2 = blockpistonstructurehelper.getBlocksToDestroy();
         int k = list.size() + list2.size();
         IBlockState[] aiblockstate = new IBlockState[k];
         EnumFacing enumfacing = extending ? direction : direction.getOpposite();

         int l;
         BlockPos blockpos3;
         IBlockState iblockstate3;
         for(l = list2.size() - 1; l >= 0; --l) {
            blockpos3 = (BlockPos)list2.get(l);
            iblockstate3 = worldIn.getBlockState(blockpos3);
            iblockstate3.getBlock().dropBlockAsItem(worldIn, blockpos3, iblockstate3, 0);
            worldIn.setBlockState(blockpos3, Blocks.AIR.getDefaultState(), 4);
            --k;
            aiblockstate[k] = iblockstate3;
         }

         for(l = list.size() - 1; l >= 0; --l) {
            blockpos3 = (BlockPos)list.get(l);
            iblockstate3 = worldIn.getBlockState(blockpos3);
            worldIn.setBlockState(blockpos3, Blocks.AIR.getDefaultState(), 2);
            blockpos3 = blockpos3.offset(enumfacing);
            worldIn.setBlockState(blockpos3, Blocks.PISTON_EXTENSION.getDefaultState().withProperty(FACING, direction), 4);
            worldIn.setTileEntity(blockpos3, BlockPistonMoving.createTilePiston((IBlockState)list1.get(l), direction, extending, false));
            --k;
            aiblockstate[k] = iblockstate3;
         }

         BlockPos blockpos2 = pos.offset(direction);
         if (extending) {
            BlockPistonExtension.EnumPistonType blockpistonextension$enumpistontype = this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT;
            iblockstate3 = Blocks.PISTON_HEAD.getDefaultState().withProperty(BlockPistonExtension.FACING, direction).withProperty(BlockPistonExtension.TYPE, blockpistonextension$enumpistontype);
            IBlockState iblockstate1 = Blocks.PISTON_EXTENSION.getDefaultState().withProperty(BlockPistonMoving.FACING, direction).withProperty(BlockPistonMoving.TYPE, this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
            worldIn.setBlockState(blockpos2, iblockstate1, 4);
            worldIn.setTileEntity(blockpos2, BlockPistonMoving.createTilePiston(iblockstate3, direction, true, true));
         }

         int j1;
         for(j1 = list2.size() - 1; j1 >= 0; --j1) {
            worldIn.notifyNeighborsOfStateChange((BlockPos)list2.get(j1), aiblockstate[k++].getBlock(), false);
         }

         for(j1 = list.size() - 1; j1 >= 0; --j1) {
            worldIn.notifyNeighborsOfStateChange((BlockPos)list.get(j1), aiblockstate[k++].getBlock(), false);
         }

         if (extending) {
            worldIn.notifyNeighborsOfStateChange(blockpos2, Blocks.PISTON_HEAD, false);
         }

         return true;
      }
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(FACING, getFacing(meta)).withProperty(EXTENDED, (meta & 8) > 0);
   }

   public int getMetaFromState(IBlockState state) {
      int i = 0;
      int i = i | ((EnumFacing)state.getValue(FACING)).getIndex();
      if ((Boolean)state.getValue(EXTENDED)) {
         i |= 8;
      }

      return i;
   }

   public IBlockState withRotation(IBlockState state, Rotation rot) {
      return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
   }

   public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
      return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
   }

   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, new IProperty[]{FACING, EXTENDED});
   }

   public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
      p_193383_2_ = this.getActualState(p_193383_2_, p_193383_1_, p_193383_3_);
      return p_193383_2_.getValue(FACING) != p_193383_4_.getOpposite() && (Boolean)p_193383_2_.getValue(EXTENDED) ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
   }
}
