package net.minecraft.block;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockStandingSign extends BlockSign {
   public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);

   public BlockStandingSign() {
      this.setDefaultState(this.blockState.getBaseState().withProperty(ROTATION, 0));
   }

   public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
      if (!worldIn.getBlockState(pos.down()).getMaterial().isSolid()) {
         this.dropBlockAsItem(worldIn, pos, state, 0);
         worldIn.setBlockToAir(pos);
      }

      super.neighborChanged(state, worldIn, pos, blockIn, p_189540_5_);
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(ROTATION, meta);
   }

   public int getMetaFromState(IBlockState state) {
      return (Integer)state.getValue(ROTATION);
   }

   public IBlockState withRotation(IBlockState state, Rotation rot) {
      return state.withProperty(ROTATION, rot.rotate((Integer)state.getValue(ROTATION), 16));
   }

   public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
      return state.withProperty(ROTATION, mirrorIn.mirrorRotation((Integer)state.getValue(ROTATION), 16));
   }

   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, new IProperty[]{ROTATION});
   }
}
