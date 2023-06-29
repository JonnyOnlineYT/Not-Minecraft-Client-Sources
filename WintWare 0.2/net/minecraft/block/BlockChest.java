package net.minecraft.block;

import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;

public class BlockChest extends BlockContainer {
   public static final PropertyDirection FACING;
   protected static final AxisAlignedBB NORTH_CHEST_AABB;
   protected static final AxisAlignedBB SOUTH_CHEST_AABB;
   protected static final AxisAlignedBB WEST_CHEST_AABB;
   protected static final AxisAlignedBB EAST_CHEST_AABB;
   protected static final AxisAlignedBB NOT_CONNECTED_AABB;
   public final BlockChest.Type chestType;

   protected BlockChest(BlockChest.Type chestTypeIn) {
      super(Material.WOOD);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
      this.chestType = chestTypeIn;
      this.setCreativeTab(chestTypeIn == BlockChest.Type.TRAP ? CreativeTabs.REDSTONE : CreativeTabs.DECORATIONS);
   }

   public boolean isOpaqueCube(IBlockState state) {
      return false;
   }

   public boolean isFullCube(IBlockState state) {
      return false;
   }

   public boolean func_190946_v(IBlockState p_190946_1_) {
      return true;
   }

   public EnumBlockRenderType getRenderType(IBlockState state) {
      return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
   }

   public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
      if (source.getBlockState(pos.north()).getBlock() == this) {
         return NORTH_CHEST_AABB;
      } else if (source.getBlockState(pos.south()).getBlock() == this) {
         return SOUTH_CHEST_AABB;
      } else if (source.getBlockState(pos.west()).getBlock() == this) {
         return WEST_CHEST_AABB;
      } else {
         return source.getBlockState(pos.east()).getBlock() == this ? EAST_CHEST_AABB : NOT_CONNECTED_AABB;
      }
   }

   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
      this.checkForSurroundingChests(worldIn, pos, state);
      Iterator var4 = EnumFacing.Plane.HORIZONTAL.iterator();

      while(var4.hasNext()) {
         EnumFacing enumfacing = (EnumFacing)var4.next();
         BlockPos blockpos = pos.offset(enumfacing);
         IBlockState iblockstate = worldIn.getBlockState(blockpos);
         if (iblockstate.getBlock() == this) {
            this.checkForSurroundingChests(worldIn, blockpos, iblockstate);
         }
      }

   }

   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
   }

   public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
      EnumFacing enumfacing = EnumFacing.getHorizontal(MathHelper.floor((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3).getOpposite();
      state = state.withProperty(FACING, enumfacing);
      BlockPos blockpos = pos.north();
      BlockPos blockpos1 = pos.south();
      BlockPos blockpos2 = pos.west();
      BlockPos blockpos3 = pos.east();
      boolean flag = this == worldIn.getBlockState(blockpos).getBlock();
      boolean flag1 = this == worldIn.getBlockState(blockpos1).getBlock();
      boolean flag2 = this == worldIn.getBlockState(blockpos2).getBlock();
      boolean flag3 = this == worldIn.getBlockState(blockpos3).getBlock();
      if (!flag && !flag1 && !flag2 && !flag3) {
         worldIn.setBlockState(pos, state, 3);
      } else if (enumfacing.getAxis() == EnumFacing.Axis.X && (flag || flag1)) {
         if (flag) {
            worldIn.setBlockState(blockpos, state, 3);
         } else {
            worldIn.setBlockState(blockpos1, state, 3);
         }

         worldIn.setBlockState(pos, state, 3);
      } else if (enumfacing.getAxis() == EnumFacing.Axis.Z && (flag2 || flag3)) {
         if (flag2) {
            worldIn.setBlockState(blockpos2, state, 3);
         } else {
            worldIn.setBlockState(blockpos3, state, 3);
         }

         worldIn.setBlockState(pos, state, 3);
      }

      if (stack.hasDisplayName()) {
         TileEntity tileentity = worldIn.getTileEntity(pos);
         if (tileentity instanceof TileEntityChest) {
            ((TileEntityChest)tileentity).func_190575_a(stack.getDisplayName());
         }
      }

   }

   public IBlockState checkForSurroundingChests(World worldIn, BlockPos pos, IBlockState state) {
      if (worldIn.isRemote) {
         return state;
      } else {
         IBlockState iblockstate = worldIn.getBlockState(pos.north());
         IBlockState iblockstate1 = worldIn.getBlockState(pos.south());
         IBlockState iblockstate2 = worldIn.getBlockState(pos.west());
         IBlockState iblockstate3 = worldIn.getBlockState(pos.east());
         EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
         if (iblockstate.getBlock() != this && iblockstate1.getBlock() != this) {
            boolean flag = iblockstate.isFullBlock();
            boolean flag1 = iblockstate1.isFullBlock();
            if (iblockstate2.getBlock() == this || iblockstate3.getBlock() == this) {
               BlockPos blockpos1 = iblockstate2.getBlock() == this ? pos.west() : pos.east();
               IBlockState iblockstate7 = worldIn.getBlockState(blockpos1.north());
               IBlockState iblockstate6 = worldIn.getBlockState(blockpos1.south());
               enumfacing = EnumFacing.SOUTH;
               EnumFacing enumfacing2;
               if (iblockstate2.getBlock() == this) {
                  enumfacing2 = (EnumFacing)iblockstate2.getValue(FACING);
               } else {
                  enumfacing2 = (EnumFacing)iblockstate3.getValue(FACING);
               }

               if (enumfacing2 == EnumFacing.NORTH) {
                  enumfacing = EnumFacing.NORTH;
               }

               if ((flag || iblockstate7.isFullBlock()) && !flag1 && !iblockstate6.isFullBlock()) {
                  enumfacing = EnumFacing.SOUTH;
               }

               if ((flag1 || iblockstate6.isFullBlock()) && !flag && !iblockstate7.isFullBlock()) {
                  enumfacing = EnumFacing.NORTH;
               }
            }
         } else {
            BlockPos blockpos = iblockstate.getBlock() == this ? pos.north() : pos.south();
            IBlockState iblockstate4 = worldIn.getBlockState(blockpos.west());
            IBlockState iblockstate5 = worldIn.getBlockState(blockpos.east());
            enumfacing = EnumFacing.EAST;
            EnumFacing enumfacing1;
            if (iblockstate.getBlock() == this) {
               enumfacing1 = (EnumFacing)iblockstate.getValue(FACING);
            } else {
               enumfacing1 = (EnumFacing)iblockstate1.getValue(FACING);
            }

            if (enumfacing1 == EnumFacing.WEST) {
               enumfacing = EnumFacing.WEST;
            }

            if ((iblockstate2.isFullBlock() || iblockstate4.isFullBlock()) && !iblockstate3.isFullBlock() && !iblockstate5.isFullBlock()) {
               enumfacing = EnumFacing.EAST;
            }

            if ((iblockstate3.isFullBlock() || iblockstate5.isFullBlock()) && !iblockstate2.isFullBlock() && !iblockstate4.isFullBlock()) {
               enumfacing = EnumFacing.WEST;
            }
         }

         state = state.withProperty(FACING, enumfacing);
         worldIn.setBlockState(pos, state, 3);
         return state;
      }
   }

   public IBlockState correctFacing(World worldIn, BlockPos pos, IBlockState state) {
      EnumFacing enumfacing = null;
      Iterator var5 = EnumFacing.Plane.HORIZONTAL.iterator();

      while(var5.hasNext()) {
         EnumFacing enumfacing1 = (EnumFacing)var5.next();
         IBlockState iblockstate = worldIn.getBlockState(pos.offset(enumfacing1));
         if (iblockstate.getBlock() == this) {
            return state;
         }

         if (iblockstate.isFullBlock()) {
            if (enumfacing != null) {
               enumfacing = null;
               break;
            }

            enumfacing = enumfacing1;
         }
      }

      if (enumfacing != null) {
         return state.withProperty(FACING, enumfacing.getOpposite());
      } else {
         EnumFacing enumfacing2 = (EnumFacing)state.getValue(FACING);
         if (worldIn.getBlockState(pos.offset(enumfacing2)).isFullBlock()) {
            enumfacing2 = enumfacing2.getOpposite();
         }

         if (worldIn.getBlockState(pos.offset(enumfacing2)).isFullBlock()) {
            enumfacing2 = enumfacing2.rotateY();
         }

         if (worldIn.getBlockState(pos.offset(enumfacing2)).isFullBlock()) {
            enumfacing2 = enumfacing2.getOpposite();
         }

         return state.withProperty(FACING, enumfacing2);
      }
   }

   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
      int i = 0;
      BlockPos blockpos = pos.west();
      BlockPos blockpos1 = pos.east();
      BlockPos blockpos2 = pos.north();
      BlockPos blockpos3 = pos.south();
      if (worldIn.getBlockState(blockpos).getBlock() == this) {
         if (this.isDoubleChest(worldIn, blockpos)) {
            return false;
         }

         ++i;
      }

      if (worldIn.getBlockState(blockpos1).getBlock() == this) {
         if (this.isDoubleChest(worldIn, blockpos1)) {
            return false;
         }

         ++i;
      }

      if (worldIn.getBlockState(blockpos2).getBlock() == this) {
         if (this.isDoubleChest(worldIn, blockpos2)) {
            return false;
         }

         ++i;
      }

      if (worldIn.getBlockState(blockpos3).getBlock() == this) {
         if (this.isDoubleChest(worldIn, blockpos3)) {
            return false;
         }

         ++i;
      }

      return i <= 1;
   }

   private boolean isDoubleChest(World worldIn, BlockPos pos) {
      if (worldIn.getBlockState(pos).getBlock() != this) {
         return false;
      } else {
         Iterator var3 = EnumFacing.Plane.HORIZONTAL.iterator();

         EnumFacing enumfacing;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            enumfacing = (EnumFacing)var3.next();
         } while(worldIn.getBlockState(pos.offset(enumfacing)).getBlock() != this);

         return true;
      }
   }

   public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
      super.neighborChanged(state, worldIn, pos, blockIn, p_189540_5_);
      TileEntity tileentity = worldIn.getTileEntity(pos);
      if (tileentity instanceof TileEntityChest) {
         tileentity.updateContainingBlockInfo();
      }

   }

   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
      TileEntity tileentity = worldIn.getTileEntity(pos);
      if (tileentity instanceof IInventory) {
         InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
         worldIn.updateComparatorOutputLevel(pos, this);
      }

      super.breakBlock(worldIn, pos, state);
   }

   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
      if (worldIn.isRemote) {
         return true;
      } else {
         ILockableContainer ilockablecontainer = this.getLockableContainer(worldIn, pos);
         if (ilockablecontainer != null) {
            playerIn.displayGUIChest(ilockablecontainer);
            if (this.chestType == BlockChest.Type.BASIC) {
               playerIn.addStat(StatList.CHEST_OPENED);
            } else if (this.chestType == BlockChest.Type.TRAP) {
               playerIn.addStat(StatList.TRAPPED_CHEST_TRIGGERED);
            }
         }

         return true;
      }
   }

   @Nullable
   public ILockableContainer getLockableContainer(World worldIn, BlockPos pos) {
      return this.getContainer(worldIn, pos, false);
   }

   @Nullable
   public ILockableContainer getContainer(World p_189418_1_, BlockPos p_189418_2_, boolean p_189418_3_) {
      TileEntity tileentity = p_189418_1_.getTileEntity(p_189418_2_);
      if (!(tileentity instanceof TileEntityChest)) {
         return null;
      } else {
         ILockableContainer ilockablecontainer = (TileEntityChest)tileentity;
         if (!p_189418_3_ && this.isBlocked(p_189418_1_, p_189418_2_)) {
            return null;
         } else {
            Iterator var6 = EnumFacing.Plane.HORIZONTAL.iterator();

            while(true) {
               while(true) {
                  EnumFacing enumfacing;
                  TileEntity tileentity1;
                  do {
                     BlockPos blockpos;
                     Block block;
                     do {
                        if (!var6.hasNext()) {
                           return (ILockableContainer)ilockablecontainer;
                        }

                        enumfacing = (EnumFacing)var6.next();
                        blockpos = p_189418_2_.offset(enumfacing);
                        block = p_189418_1_.getBlockState(blockpos).getBlock();
                     } while(block != this);

                     if (this.isBlocked(p_189418_1_, blockpos)) {
                        return null;
                     }

                     tileentity1 = p_189418_1_.getTileEntity(blockpos);
                  } while(!(tileentity1 instanceof TileEntityChest));

                  if (enumfacing != EnumFacing.WEST && enumfacing != EnumFacing.NORTH) {
                     ilockablecontainer = new InventoryLargeChest("container.chestDouble", (ILockableContainer)ilockablecontainer, (TileEntityChest)tileentity1);
                  } else {
                     ilockablecontainer = new InventoryLargeChest("container.chestDouble", (TileEntityChest)tileentity1, (ILockableContainer)ilockablecontainer);
                  }
               }
            }
         }
      }
   }

   public TileEntity createNewTileEntity(World worldIn, int meta) {
      return new TileEntityChest();
   }

   public boolean canProvidePower(IBlockState state) {
      return this.chestType == BlockChest.Type.TRAP;
   }

   public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
      if (!blockState.canProvidePower()) {
         return 0;
      } else {
         int i = 0;
         TileEntity tileentity = blockAccess.getTileEntity(pos);
         if (tileentity instanceof TileEntityChest) {
            i = ((TileEntityChest)tileentity).numPlayersUsing;
         }

         return MathHelper.clamp(i, 0, 15);
      }
   }

   public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
      return side == EnumFacing.UP ? blockState.getWeakPower(blockAccess, pos, side) : 0;
   }

   private boolean isBlocked(World worldIn, BlockPos pos) {
      return this.isBelowSolidBlock(worldIn, pos) || this.isOcelotSittingOnChest(worldIn, pos);
   }

   private boolean isBelowSolidBlock(World worldIn, BlockPos pos) {
      return worldIn.getBlockState(pos.up()).isNormalCube();
   }

   private boolean isOcelotSittingOnChest(World worldIn, BlockPos pos) {
      Iterator var3 = worldIn.getEntitiesWithinAABB(EntityOcelot.class, new AxisAlignedBB((double)pos.getX(), (double)(pos.getY() + 1), (double)pos.getZ(), (double)(pos.getX() + 1), (double)(pos.getY() + 2), (double)(pos.getZ() + 1))).iterator();

      EntityOcelot entityocelot;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         Entity entity = (Entity)var3.next();
         entityocelot = (EntityOcelot)entity;
      } while(!entityocelot.isSitting());

      return true;
   }

   public boolean hasComparatorInputOverride(IBlockState state) {
      return true;
   }

   public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
      return Container.calcRedstoneFromInventory(this.getLockableContainer(worldIn, pos));
   }

   public IBlockState getStateFromMeta(int meta) {
      EnumFacing enumfacing = EnumFacing.getFront(meta);
      if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
         enumfacing = EnumFacing.NORTH;
      }

      return this.getDefaultState().withProperty(FACING, enumfacing);
   }

   public int getMetaFromState(IBlockState state) {
      return ((EnumFacing)state.getValue(FACING)).getIndex();
   }

   public IBlockState withRotation(IBlockState state, Rotation rot) {
      return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
   }

   public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
      return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
   }

   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, new IProperty[]{FACING});
   }

   public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
      return BlockFaceShape.UNDEFINED;
   }

   static {
      FACING = BlockHorizontal.FACING;
      NORTH_CHEST_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0D, 0.9375D, 0.875D, 0.9375D);
      SOUTH_CHEST_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 1.0D);
      WEST_CHEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
      EAST_CHEST_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 1.0D, 0.875D, 0.9375D);
      NOT_CONNECTED_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
   }

   public static enum Type {
      BASIC,
      TRAP;
   }
}
