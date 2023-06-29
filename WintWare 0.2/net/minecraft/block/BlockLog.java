package net.minecraft.block;

import java.util.Iterator;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockLog extends BlockRotatedPillar {
   public static final PropertyEnum<BlockLog.EnumAxis> LOG_AXIS = PropertyEnum.create("axis", BlockLog.EnumAxis.class);

   public BlockLog() {
      super(Material.WOOD);
      this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
      this.setHardness(2.0F);
      this.setSoundType(SoundType.WOOD);
   }

   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
      int i = true;
      int j = true;
      if (worldIn.isAreaLoaded(pos.add(-5, -5, -5), pos.add(5, 5, 5))) {
         Iterator var6 = BlockPos.getAllInBox(pos.add(-4, -4, -4), pos.add(4, 4, 4)).iterator();

         while(var6.hasNext()) {
            BlockPos blockpos = (BlockPos)var6.next();
            IBlockState iblockstate = worldIn.getBlockState(blockpos);
            if (iblockstate.getMaterial() == Material.LEAVES && !(Boolean)iblockstate.getValue(BlockLeaves.CHECK_DECAY)) {
               worldIn.setBlockState(blockpos, iblockstate.withProperty(BlockLeaves.CHECK_DECAY, true), 4);
            }
         }
      }

   }

   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      return this.getStateFromMeta(meta).withProperty(LOG_AXIS, BlockLog.EnumAxis.fromFacingAxis(facing.getAxis()));
   }

   public IBlockState withRotation(IBlockState state, Rotation rot) {
      switch(rot) {
      case COUNTERCLOCKWISE_90:
      case CLOCKWISE_90:
         switch((BlockLog.EnumAxis)state.getValue(LOG_AXIS)) {
         case X:
            return state.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
         case Z:
            return state.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
         default:
            return state;
         }
      default:
         return state;
      }
   }

   public static enum EnumAxis implements IStringSerializable {
      X("x"),
      Y("y"),
      Z("z"),
      NONE("none");

      private final String name;

      private EnumAxis(String name) {
         this.name = name;
      }

      public String toString() {
         return this.name;
      }

      public static BlockLog.EnumAxis fromFacingAxis(EnumFacing.Axis axis) {
         switch(axis) {
         case X:
            return X;
         case Y:
            return Y;
         case Z:
            return Z;
         default:
            return NONE;
         }
      }

      public String getName() {
         return this.name;
      }
   }
}
