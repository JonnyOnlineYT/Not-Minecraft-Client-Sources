package net.minecraft.block;

import java.util.Iterator;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockVine extends Block {
   public static final PropertyBool UP = PropertyBool.create("up");
   public static final PropertyBool NORTH = PropertyBool.create("north");
   public static final PropertyBool EAST = PropertyBool.create("east");
   public static final PropertyBool SOUTH = PropertyBool.create("south");
   public static final PropertyBool WEST = PropertyBool.create("west");
   public static final PropertyBool[] ALL_FACES;
   protected static final AxisAlignedBB UP_AABB;
   protected static final AxisAlignedBB WEST_AABB;
   protected static final AxisAlignedBB EAST_AABB;
   protected static final AxisAlignedBB NORTH_AABB;
   protected static final AxisAlignedBB SOUTH_AABB;

   public BlockVine() {
      super(Material.VINE);
      this.setDefaultState(this.blockState.getBaseState().withProperty(UP, false).withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false));
      this.setTickRandomly(true);
      this.setCreativeTab(CreativeTabs.DECORATIONS);
   }

   @Nullable
   public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
      return NULL_AABB;
   }

   public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
      state = state.getActualState(source, pos);
      int i = 0;
      AxisAlignedBB axisalignedbb = FULL_BLOCK_AABB;
      if ((Boolean)state.getValue(UP)) {
         axisalignedbb = UP_AABB;
         ++i;
      }

      if ((Boolean)state.getValue(NORTH)) {
         axisalignedbb = NORTH_AABB;
         ++i;
      }

      if ((Boolean)state.getValue(EAST)) {
         axisalignedbb = EAST_AABB;
         ++i;
      }

      if ((Boolean)state.getValue(SOUTH)) {
         axisalignedbb = SOUTH_AABB;
         ++i;
      }

      if ((Boolean)state.getValue(WEST)) {
         axisalignedbb = WEST_AABB;
         ++i;
      }

      return i == 1 ? axisalignedbb : FULL_BLOCK_AABB;
   }

   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
      BlockPos blockpos = pos.up();
      return state.withProperty(UP, worldIn.getBlockState(blockpos).func_193401_d(worldIn, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID);
   }

   public boolean isOpaqueCube(IBlockState state) {
      return false;
   }

   public boolean isFullCube(IBlockState state) {
      return false;
   }

   public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
      return true;
   }

   public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
      return side != EnumFacing.DOWN && side != EnumFacing.UP && this.func_193395_a(worldIn, pos, side);
   }

   public boolean func_193395_a(World p_193395_1_, BlockPos p_193395_2_, EnumFacing p_193395_3_) {
      Block block = p_193395_1_.getBlockState(p_193395_2_.up()).getBlock();
      return this.func_193396_c(p_193395_1_, p_193395_2_.offset(p_193395_3_.getOpposite()), p_193395_3_) && (block == Blocks.AIR || block == Blocks.VINE || this.func_193396_c(p_193395_1_, p_193395_2_.up(), EnumFacing.UP));
   }

   private boolean func_193396_c(World p_193396_1_, BlockPos p_193396_2_, EnumFacing p_193396_3_) {
      IBlockState iblockstate = p_193396_1_.getBlockState(p_193396_2_);
      return iblockstate.func_193401_d(p_193396_1_, p_193396_2_, p_193396_3_) == BlockFaceShape.SOLID && !func_193397_e(iblockstate.getBlock());
   }

   protected static boolean func_193397_e(Block p_193397_0_) {
      return p_193397_0_ instanceof BlockShulkerBox || p_193397_0_ == Blocks.BEACON || p_193397_0_ == Blocks.CAULDRON || p_193397_0_ == Blocks.GLASS || p_193397_0_ == Blocks.STAINED_GLASS || p_193397_0_ == Blocks.PISTON || p_193397_0_ == Blocks.STICKY_PISTON || p_193397_0_ == Blocks.PISTON_HEAD || p_193397_0_ == Blocks.TRAPDOOR;
   }

   private boolean recheckGrownSides(World worldIn, BlockPos pos, IBlockState state) {
      IBlockState iblockstate = state;
      Iterator var5 = EnumFacing.Plane.HORIZONTAL.iterator();

      while(true) {
         PropertyBool propertybool;
         IBlockState iblockstate1;
         do {
            EnumFacing enumfacing;
            do {
               do {
                  if (!var5.hasNext()) {
                     if (getNumGrownFaces(state) == 0) {
                        return false;
                     }

                     if (iblockstate != state) {
                        worldIn.setBlockState(pos, state, 2);
                     }

                     return true;
                  }

                  enumfacing = (EnumFacing)var5.next();
                  propertybool = getPropertyFor(enumfacing);
               } while(!(Boolean)state.getValue(propertybool));
            } while(this.func_193395_a(worldIn, pos, enumfacing.getOpposite()));

            iblockstate1 = worldIn.getBlockState(pos.up());
         } while(iblockstate1.getBlock() == this && (Boolean)iblockstate1.getValue(propertybool));

         state = state.withProperty(propertybool, false);
      }
   }

   public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
      if (!worldIn.isRemote && !this.recheckGrownSides(worldIn, pos, state)) {
         this.dropBlockAsItem(worldIn, pos, state, 0);
         worldIn.setBlockToAir(pos);
      }

   }

   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
      if (!worldIn.isRemote && worldIn.rand.nextInt(4) == 0) {
         int i = true;
         int j = 5;
         boolean flag = false;

         label176:
         for(int k = -4; k <= 4; ++k) {
            for(int l = -4; l <= 4; ++l) {
               for(int i1 = -1; i1 <= 1; ++i1) {
                  if (worldIn.getBlockState(pos.add(k, i1, l)).getBlock() == this) {
                     --j;
                     if (j <= 0) {
                        flag = true;
                        break label176;
                     }
                  }
               }
            }
         }

         EnumFacing enumfacing1 = EnumFacing.random(rand);
         BlockPos blockpos2 = pos.up();
         if (enumfacing1 == EnumFacing.UP && pos.getY() < 255 && worldIn.isAirBlock(blockpos2)) {
            IBlockState iblockstate2 = state;
            Iterator var23 = EnumFacing.Plane.HORIZONTAL.iterator();

            while(true) {
               while(var23.hasNext()) {
                  EnumFacing enumfacing2 = (EnumFacing)var23.next();
                  if (rand.nextBoolean() && this.func_193395_a(worldIn, blockpos2, enumfacing2.getOpposite())) {
                     iblockstate2 = iblockstate2.withProperty(getPropertyFor(enumfacing2), true);
                  } else {
                     iblockstate2 = iblockstate2.withProperty(getPropertyFor(enumfacing2), false);
                  }
               }

               if ((Boolean)iblockstate2.getValue(NORTH) || (Boolean)iblockstate2.getValue(EAST) || (Boolean)iblockstate2.getValue(SOUTH) || (Boolean)iblockstate2.getValue(WEST)) {
                  worldIn.setBlockState(blockpos2, iblockstate2, 2);
               }
               break;
            }
         } else {
            IBlockState iblockstate;
            Block block;
            BlockPos blockpos3;
            if (enumfacing1.getAxis().isHorizontal() && !(Boolean)state.getValue(getPropertyFor(enumfacing1))) {
               if (!flag) {
                  blockpos3 = pos.offset(enumfacing1);
                  iblockstate = worldIn.getBlockState(blockpos3);
                  block = iblockstate.getBlock();
                  if (block.blockMaterial == Material.AIR) {
                     EnumFacing enumfacing3 = enumfacing1.rotateY();
                     EnumFacing enumfacing4 = enumfacing1.rotateYCCW();
                     boolean flag1 = (Boolean)state.getValue(getPropertyFor(enumfacing3));
                     boolean flag2 = (Boolean)state.getValue(getPropertyFor(enumfacing4));
                     BlockPos blockpos = blockpos3.offset(enumfacing3);
                     BlockPos blockpos1 = blockpos3.offset(enumfacing4);
                     if (flag1 && this.func_193395_a(worldIn, blockpos.offset(enumfacing3), enumfacing3)) {
                        worldIn.setBlockState(blockpos3, this.getDefaultState().withProperty(getPropertyFor(enumfacing3), true), 2);
                     } else if (flag2 && this.func_193395_a(worldIn, blockpos1.offset(enumfacing4), enumfacing4)) {
                        worldIn.setBlockState(blockpos3, this.getDefaultState().withProperty(getPropertyFor(enumfacing4), true), 2);
                     } else if (flag1 && worldIn.isAirBlock(blockpos) && this.func_193395_a(worldIn, blockpos, enumfacing1)) {
                        worldIn.setBlockState(blockpos, this.getDefaultState().withProperty(getPropertyFor(enumfacing1.getOpposite()), true), 2);
                     } else if (flag2 && worldIn.isAirBlock(blockpos1) && this.func_193395_a(worldIn, blockpos1, enumfacing1)) {
                        worldIn.setBlockState(blockpos1, this.getDefaultState().withProperty(getPropertyFor(enumfacing1.getOpposite()), true), 2);
                     }
                  } else if (iblockstate.func_193401_d(worldIn, blockpos3, enumfacing1) == BlockFaceShape.SOLID) {
                     worldIn.setBlockState(pos, state.withProperty(getPropertyFor(enumfacing1), true), 2);
                  }
               }
            } else if (pos.getY() > 1) {
               blockpos3 = pos.down();
               iblockstate = worldIn.getBlockState(blockpos3);
               block = iblockstate.getBlock();
               IBlockState iblockstate4;
               Iterator var14;
               EnumFacing enumfacing;
               if (block.blockMaterial == Material.AIR) {
                  iblockstate4 = state;
                  var14 = EnumFacing.Plane.HORIZONTAL.iterator();

                  while(var14.hasNext()) {
                     enumfacing = (EnumFacing)var14.next();
                     if (rand.nextBoolean()) {
                        iblockstate4 = iblockstate4.withProperty(getPropertyFor(enumfacing), false);
                     }
                  }

                  if ((Boolean)iblockstate4.getValue(NORTH) || (Boolean)iblockstate4.getValue(EAST) || (Boolean)iblockstate4.getValue(SOUTH) || (Boolean)iblockstate4.getValue(WEST)) {
                     worldIn.setBlockState(blockpos3, iblockstate4, 2);
                  }
               } else if (block == this) {
                  iblockstate4 = iblockstate;
                  var14 = EnumFacing.Plane.HORIZONTAL.iterator();

                  while(var14.hasNext()) {
                     enumfacing = (EnumFacing)var14.next();
                     PropertyBool propertybool = getPropertyFor(enumfacing);
                     if (rand.nextBoolean() && (Boolean)state.getValue(propertybool)) {
                        iblockstate4 = iblockstate4.withProperty(propertybool, true);
                     }
                  }

                  if ((Boolean)iblockstate4.getValue(NORTH) || (Boolean)iblockstate4.getValue(EAST) || (Boolean)iblockstate4.getValue(SOUTH) || (Boolean)iblockstate4.getValue(WEST)) {
                     worldIn.setBlockState(blockpos3, iblockstate4, 2);
                  }
               }
            }
         }
      }

   }

   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      IBlockState iblockstate = this.getDefaultState().withProperty(UP, false).withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false);
      return facing.getAxis().isHorizontal() ? iblockstate.withProperty(getPropertyFor(facing.getOpposite()), true) : iblockstate;
   }

   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
      return Items.field_190931_a;
   }

   public int quantityDropped(Random random) {
      return 0;
   }

   public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
      if (!worldIn.isRemote && stack.getItem() == Items.SHEARS) {
         player.addStat(StatList.getBlockStats(this));
         spawnAsEntity(worldIn, pos, new ItemStack(Blocks.VINE, 1, 0));
      } else {
         super.harvestBlock(worldIn, player, pos, state, te, stack);
      }

   }

   public BlockRenderLayer getBlockLayer() {
      return BlockRenderLayer.CUTOUT;
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(SOUTH, (meta & 1) > 0).withProperty(WEST, (meta & 2) > 0).withProperty(NORTH, (meta & 4) > 0).withProperty(EAST, (meta & 8) > 0);
   }

   public int getMetaFromState(IBlockState state) {
      int i = 0;
      if ((Boolean)state.getValue(SOUTH)) {
         i |= 1;
      }

      if ((Boolean)state.getValue(WEST)) {
         i |= 2;
      }

      if ((Boolean)state.getValue(NORTH)) {
         i |= 4;
      }

      if ((Boolean)state.getValue(EAST)) {
         i |= 8;
      }

      return i;
   }

   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, new IProperty[]{UP, NORTH, EAST, SOUTH, WEST});
   }

   public IBlockState withRotation(IBlockState state, Rotation rot) {
      switch(rot) {
      case CLOCKWISE_180:
         return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(EAST, state.getValue(WEST)).withProperty(SOUTH, state.getValue(NORTH)).withProperty(WEST, state.getValue(EAST));
      case COUNTERCLOCKWISE_90:
         return state.withProperty(NORTH, state.getValue(EAST)).withProperty(EAST, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(WEST)).withProperty(WEST, state.getValue(NORTH));
      case CLOCKWISE_90:
         return state.withProperty(NORTH, state.getValue(WEST)).withProperty(EAST, state.getValue(NORTH)).withProperty(SOUTH, state.getValue(EAST)).withProperty(WEST, state.getValue(SOUTH));
      default:
         return state;
      }
   }

   public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
      switch(mirrorIn) {
      case LEFT_RIGHT:
         return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(NORTH));
      case FRONT_BACK:
         return state.withProperty(EAST, state.getValue(WEST)).withProperty(WEST, state.getValue(EAST));
      default:
         return super.withMirror(state, mirrorIn);
      }
   }

   public static PropertyBool getPropertyFor(EnumFacing side) {
      switch(side) {
      case UP:
         return UP;
      case NORTH:
         return NORTH;
      case SOUTH:
         return SOUTH;
      case WEST:
         return WEST;
      case EAST:
         return EAST;
      default:
         throw new IllegalArgumentException(side + " is an invalid choice");
      }
   }

   public static int getNumGrownFaces(IBlockState state) {
      int i = 0;
      PropertyBool[] var2 = ALL_FACES;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         PropertyBool propertybool = var2[var4];
         if ((Boolean)state.getValue(propertybool)) {
            ++i;
         }
      }

      return i;
   }

   public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
      return BlockFaceShape.UNDEFINED;
   }

   static {
      ALL_FACES = new PropertyBool[]{UP, NORTH, SOUTH, WEST, EAST};
      UP_AABB = new AxisAlignedBB(0.0D, 0.9375D, 0.0D, 1.0D, 1.0D, 1.0D);
      WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D, 1.0D);
      EAST_AABB = new AxisAlignedBB(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
      NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0625D);
      SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.9375D, 1.0D, 1.0D, 1.0D);
   }
}
