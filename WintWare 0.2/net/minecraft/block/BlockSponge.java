package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class BlockSponge extends Block {
   public static final PropertyBool WET = PropertyBool.create("wet");

   protected BlockSponge() {
      super(Material.SPONGE);
      this.setDefaultState(this.blockState.getBaseState().withProperty(WET, false));
      this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
   }

   public String getLocalizedName() {
      return I18n.translateToLocal(this.getUnlocalizedName() + ".dry.name");
   }

   public int damageDropped(IBlockState state) {
      return (Boolean)state.getValue(WET) ? 1 : 0;
   }

   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
      this.tryAbsorb(worldIn, pos, state);
   }

   public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
      this.tryAbsorb(worldIn, pos, state);
      super.neighborChanged(state, worldIn, pos, blockIn, p_189540_5_);
   }

   protected void tryAbsorb(World worldIn, BlockPos pos, IBlockState state) {
      if (!(Boolean)state.getValue(WET) && this.absorb(worldIn, pos)) {
         worldIn.setBlockState(pos, state.withProperty(WET, true), 2);
         worldIn.playEvent(2001, pos, Block.getIdFromBlock(Blocks.WATER));
      }

   }

   private boolean absorb(World worldIn, BlockPos pos) {
      Queue<Tuple<BlockPos, Integer>> queue = Lists.newLinkedList();
      List<BlockPos> list = Lists.newArrayList();
      queue.add(new Tuple(pos, 0));
      int i = 0;

      BlockPos blockpos;
      while(!queue.isEmpty()) {
         Tuple<BlockPos, Integer> tuple = (Tuple)queue.poll();
         blockpos = (BlockPos)tuple.getFirst();
         int j = (Integer)tuple.getSecond();
         EnumFacing[] var9 = EnumFacing.values();
         int var10 = var9.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            EnumFacing enumfacing = var9[var11];
            BlockPos blockpos1 = blockpos.offset(enumfacing);
            if (worldIn.getBlockState(blockpos1).getMaterial() == Material.WATER) {
               worldIn.setBlockState(blockpos1, Blocks.AIR.getDefaultState(), 2);
               list.add(blockpos1);
               ++i;
               if (j < 6) {
                  queue.add(new Tuple(blockpos1, j + 1));
               }
            }
         }

         if (i > 64) {
            break;
         }
      }

      Iterator var14 = list.iterator();

      while(var14.hasNext()) {
         blockpos = (BlockPos)var14.next();
         worldIn.notifyNeighborsOfStateChange(blockpos, Blocks.AIR, false);
      }

      return i > 0;
   }

   public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
      tab.add(new ItemStack(this, 1, 0));
      tab.add(new ItemStack(this, 1, 1));
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(WET, (meta & 1) == 1);
   }

   public int getMetaFromState(IBlockState state) {
      return (Boolean)state.getValue(WET) ? 1 : 0;
   }

   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, new IProperty[]{WET});
   }

   public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
      if ((Boolean)stateIn.getValue(WET)) {
         EnumFacing enumfacing = EnumFacing.random(rand);
         if (enumfacing != EnumFacing.UP && !worldIn.getBlockState(pos.offset(enumfacing)).isFullyOpaque()) {
            double d0 = (double)pos.getX();
            double d1 = (double)pos.getY();
            double d2 = (double)pos.getZ();
            if (enumfacing == EnumFacing.DOWN) {
               d1 -= 0.05D;
               d0 += rand.nextDouble();
               d2 += rand.nextDouble();
            } else {
               d1 += rand.nextDouble() * 0.8D;
               if (enumfacing.getAxis() == EnumFacing.Axis.X) {
                  d2 += rand.nextDouble();
                  if (enumfacing == EnumFacing.EAST) {
                     ++d0;
                  } else {
                     d0 += 0.05D;
                  }
               } else {
                  d0 += rand.nextDouble();
                  if (enumfacing == EnumFacing.SOUTH) {
                     ++d2;
                  } else {
                     d2 += 0.05D;
                  }
               }
            }

            worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, d0, d1, d2, 0.0D, 0.0D, 0.0D);
         }
      }

   }
}
