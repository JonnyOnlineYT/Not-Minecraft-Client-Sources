package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BlockAnvil extends BlockFalling {
   public static final PropertyDirection FACING;
   public static final PropertyInteger DAMAGE;
   protected static final AxisAlignedBB X_AXIS_AABB;
   protected static final AxisAlignedBB Z_AXIS_AABB;
   protected static final Logger LOGGER;

   protected BlockAnvil() {
      super(Material.ANVIL);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(DAMAGE, 0));
      this.setLightOpacity(0);
      this.setCreativeTab(CreativeTabs.DECORATIONS);
   }

   public boolean isFullCube(IBlockState state) {
      return false;
   }

   public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
      return BlockFaceShape.UNDEFINED;
   }

   public boolean isOpaqueCube(IBlockState state) {
      return false;
   }

   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      EnumFacing enumfacing = placer.getHorizontalFacing().rotateY();

      try {
         return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, enumfacing).withProperty(DAMAGE, meta >> 2);
      } catch (IllegalArgumentException var11) {
         if (!worldIn.isRemote) {
            LOGGER.warn(String.format("Invalid damage property for anvil at %s. Found %d, must be in [0, 1, 2]", pos, meta >> 2));
            if (placer instanceof EntityPlayer) {
               placer.addChatMessage(new TextComponentTranslation("Invalid damage property. Please pick in [0, 1, 2]", new Object[0]));
            }
         }

         return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, 0, placer).withProperty(FACING, enumfacing).withProperty(DAMAGE, 0);
      }
   }

   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
      if (!worldIn.isRemote) {
         playerIn.displayGui(new BlockAnvil.Anvil(worldIn, pos));
      }

      return true;
   }

   public int damageDropped(IBlockState state) {
      return (Integer)state.getValue(DAMAGE);
   }

   public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
      EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
      return enumfacing.getAxis() == EnumFacing.Axis.X ? X_AXIS_AABB : Z_AXIS_AABB;
   }

   public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
      tab.add(new ItemStack(this));
      tab.add(new ItemStack(this, 1, 1));
      tab.add(new ItemStack(this, 1, 2));
   }

   protected void onStartFalling(EntityFallingBlock fallingEntity) {
      fallingEntity.setHurtEntities(true);
   }

   public void onEndFalling(World worldIn, BlockPos pos, IBlockState p_176502_3_, IBlockState p_176502_4_) {
      worldIn.playEvent(1031, pos, 0);
   }

   public void func_190974_b(World p_190974_1_, BlockPos p_190974_2_) {
      p_190974_1_.playEvent(1029, p_190974_2_, 0);
   }

   public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
      return true;
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3)).withProperty(DAMAGE, (meta & 15) >> 2);
   }

   public int getMetaFromState(IBlockState state) {
      int i = 0;
      int i = i | ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
      i |= (Integer)state.getValue(DAMAGE) << 2;
      return i;
   }

   public IBlockState withRotation(IBlockState state, Rotation rot) {
      return state.getBlock() != this ? state : state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
   }

   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, new IProperty[]{FACING, DAMAGE});
   }

   static {
      FACING = BlockHorizontal.FACING;
      DAMAGE = PropertyInteger.create("damage", 0, 2);
      X_AXIS_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.125D, 1.0D, 1.0D, 0.875D);
      Z_AXIS_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.0D, 0.875D, 1.0D, 1.0D);
      LOGGER = LogManager.getLogger();
   }

   public static class Anvil implements IInteractionObject {
      private final World world;
      private final BlockPos position;

      public Anvil(World worldIn, BlockPos pos) {
         this.world = worldIn;
         this.position = pos;
      }

      public String getName() {
         return "anvil";
      }

      public boolean hasCustomName() {
         return false;
      }

      public ITextComponent getDisplayName() {
         return new TextComponentTranslation(Blocks.ANVIL.getUnlocalizedName() + ".name", new Object[0]);
      }

      public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
         return new ContainerRepair(playerInventory, this.world, this.position, playerIn);
      }

      public String getGuiID() {
         return "minecraft:anvil";
      }
   }
}
