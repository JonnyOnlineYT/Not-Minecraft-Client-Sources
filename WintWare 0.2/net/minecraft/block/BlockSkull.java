package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.Iterator;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMaterialMatcher;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSkull extends BlockContainer {
   public static final PropertyDirection FACING;
   public static final PropertyBool NODROP;
   private static final Predicate<BlockWorldState> IS_WITHER_SKELETON;
   protected static final AxisAlignedBB DEFAULT_AABB;
   protected static final AxisAlignedBB NORTH_AABB;
   protected static final AxisAlignedBB SOUTH_AABB;
   protected static final AxisAlignedBB WEST_AABB;
   protected static final AxisAlignedBB EAST_AABB;
   private BlockPattern witherBasePattern;
   private BlockPattern witherPattern;

   protected BlockSkull() {
      super(Material.CIRCUITS);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(NODROP, false));
   }

   public String getLocalizedName() {
      return I18n.translateToLocal("tile.skull.skeleton.name");
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

   public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
      switch((EnumFacing)state.getValue(FACING)) {
      case UP:
      default:
         return DEFAULT_AABB;
      case NORTH:
         return NORTH_AABB;
      case SOUTH:
         return SOUTH_AABB;
      case WEST:
         return WEST_AABB;
      case EAST:
         return EAST_AABB;
      }
   }

   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(NODROP, false);
   }

   public TileEntity createNewTileEntity(World worldIn, int meta) {
      return new TileEntitySkull();
   }

   public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
      int i = 0;
      TileEntity tileentity = worldIn.getTileEntity(pos);
      if (tileentity instanceof TileEntitySkull) {
         i = ((TileEntitySkull)tileentity).getSkullType();
      }

      return new ItemStack(Items.SKULL, 1, i);
   }

   public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
   }

   public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
      if (player.capabilities.isCreativeMode) {
         state = state.withProperty(NODROP, true);
         worldIn.setBlockState(pos, state, 4);
      }

      super.onBlockHarvested(worldIn, pos, state, player);
   }

   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
      if (!worldIn.isRemote) {
         if (!(Boolean)state.getValue(NODROP)) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileEntitySkull) {
               TileEntitySkull tileentityskull = (TileEntitySkull)tileentity;
               ItemStack itemstack = this.getItem(worldIn, pos, state);
               if (tileentityskull.getSkullType() == 3 && tileentityskull.getPlayerProfile() != null) {
                  itemstack.setTagCompound(new NBTTagCompound());
                  NBTTagCompound nbttagcompound = new NBTTagCompound();
                  NBTUtil.writeGameProfile(nbttagcompound, tileentityskull.getPlayerProfile());
                  itemstack.getTagCompound().setTag("SkullOwner", nbttagcompound);
               }

               spawnAsEntity(worldIn, pos, itemstack);
            }
         }

         super.breakBlock(worldIn, pos, state);
      }

   }

   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
      return Items.SKULL;
   }

   public boolean canDispenserPlace(World worldIn, BlockPos pos, ItemStack stack) {
      if (stack.getMetadata() == 1 && pos.getY() >= 2 && worldIn.getDifficulty() != EnumDifficulty.PEACEFUL && !worldIn.isRemote) {
         return this.getWitherBasePattern().match(worldIn, pos) != null;
      } else {
         return false;
      }
   }

   public void checkWitherSpawn(World worldIn, BlockPos pos, TileEntitySkull te) {
      if (te.getSkullType() == 1 && pos.getY() >= 2 && worldIn.getDifficulty() != EnumDifficulty.PEACEFUL && !worldIn.isRemote) {
         BlockPattern blockpattern = this.getWitherPattern();
         BlockPattern.PatternHelper blockpattern$patternhelper = blockpattern.match(worldIn, pos);
         if (blockpattern$patternhelper != null) {
            int j;
            for(j = 0; j < 3; ++j) {
               BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(j, 0, 0);
               worldIn.setBlockState(blockworldstate.getPos(), blockworldstate.getBlockState().withProperty(NODROP, true), 2);
            }

            for(j = 0; j < blockpattern.getPalmLength(); ++j) {
               for(int k = 0; k < blockpattern.getThumbLength(); ++k) {
                  BlockWorldState blockworldstate1 = blockpattern$patternhelper.translateOffset(j, k, 0);
                  worldIn.setBlockState(blockworldstate1.getPos(), Blocks.AIR.getDefaultState(), 2);
               }
            }

            BlockPos blockpos = blockpattern$patternhelper.translateOffset(1, 0, 0).getPos();
            EntityWither entitywither = new EntityWither(worldIn);
            BlockPos blockpos1 = blockpattern$patternhelper.translateOffset(1, 2, 0).getPos();
            entitywither.setLocationAndAngles((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.55D, (double)blockpos1.getZ() + 0.5D, blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X ? 0.0F : 90.0F, 0.0F);
            entitywither.renderYawOffset = blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X ? 0.0F : 90.0F;
            entitywither.ignite();
            Iterator var9 = worldIn.getEntitiesWithinAABB(EntityPlayerMP.class, entitywither.getEntityBoundingBox().expandXyz(50.0D)).iterator();

            while(var9.hasNext()) {
               EntityPlayerMP entityplayermp = (EntityPlayerMP)var9.next();
               CriteriaTriggers.field_192133_m.func_192229_a(entityplayermp, entitywither);
            }

            worldIn.spawnEntityInWorld(entitywither);

            int i1;
            for(i1 = 0; i1 < 120; ++i1) {
               worldIn.spawnParticle(EnumParticleTypes.SNOWBALL, (double)blockpos.getX() + worldIn.rand.nextDouble(), (double)(blockpos.getY() - 2) + worldIn.rand.nextDouble() * 3.9D, (double)blockpos.getZ() + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
            }

            for(i1 = 0; i1 < blockpattern.getPalmLength(); ++i1) {
               for(int j1 = 0; j1 < blockpattern.getThumbLength(); ++j1) {
                  BlockWorldState blockworldstate2 = blockpattern$patternhelper.translateOffset(i1, j1, 0);
                  worldIn.notifyNeighborsRespectDebug(blockworldstate2.getPos(), Blocks.AIR, false);
               }
            }
         }
      }

   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7)).withProperty(NODROP, (meta & 8) > 0);
   }

   public int getMetaFromState(IBlockState state) {
      int i = 0;
      int i = i | ((EnumFacing)state.getValue(FACING)).getIndex();
      if ((Boolean)state.getValue(NODROP)) {
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
      return new BlockStateContainer(this, new IProperty[]{FACING, NODROP});
   }

   protected BlockPattern getWitherBasePattern() {
      if (this.witherBasePattern == null) {
         this.witherBasePattern = FactoryBlockPattern.start().aisle("   ", "###", "~#~").where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.SOUL_SAND))).where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
      }

      return this.witherBasePattern;
   }

   protected BlockPattern getWitherPattern() {
      if (this.witherPattern == null) {
         this.witherPattern = FactoryBlockPattern.start().aisle("^^^", "###", "~#~").where('#', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.SOUL_SAND))).where('^', IS_WITHER_SKELETON).where('~', BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).build();
      }

      return this.witherPattern;
   }

   public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
      return BlockFaceShape.UNDEFINED;
   }

   static {
      FACING = BlockDirectional.FACING;
      NODROP = PropertyBool.create("nodrop");
      IS_WITHER_SKELETON = new Predicate<BlockWorldState>() {
         public boolean apply(@Nullable BlockWorldState p_apply_1_) {
            return p_apply_1_.getBlockState() != null && p_apply_1_.getBlockState().getBlock() == Blocks.SKULL && p_apply_1_.getTileEntity() instanceof TileEntitySkull && ((TileEntitySkull)p_apply_1_.getTileEntity()).getSkullType() == 1;
         }
      };
      DEFAULT_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.5D, 0.75D);
      NORTH_AABB = new AxisAlignedBB(0.25D, 0.25D, 0.5D, 0.75D, 0.75D, 1.0D);
      SOUTH_AABB = new AxisAlignedBB(0.25D, 0.25D, 0.0D, 0.75D, 0.75D, 0.5D);
      WEST_AABB = new AxisAlignedBB(0.5D, 0.25D, 0.25D, 1.0D, 0.75D, 0.75D);
      EAST_AABB = new AxisAlignedBB(0.0D, 0.25D, 0.25D, 0.5D, 0.75D, 0.75D);
   }
}
