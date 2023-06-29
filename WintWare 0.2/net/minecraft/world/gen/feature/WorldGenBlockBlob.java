package net.minecraft.world.gen.feature;

import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenBlockBlob extends WorldGenerator {
   private final Block block;
   private final int startRadius;

   public WorldGenBlockBlob(Block blockIn, int startRadiusIn) {
      super(false);
      this.block = blockIn;
      this.startRadius = startRadiusIn;
   }

   public boolean generate(World worldIn, Random rand, BlockPos position) {
      while(true) {
         label51: {
            if (position.getY() > 3) {
               if (worldIn.isAirBlock(position.down())) {
                  break label51;
               }

               Block block = worldIn.getBlockState(position.down()).getBlock();
               if (block != Blocks.GRASS && block != Blocks.DIRT && block != Blocks.STONE) {
                  break label51;
               }
            }

            if (position.getY() <= 3) {
               return false;
            }

            int i1 = this.startRadius;

            for(int i = 0; i1 >= 0 && i < 3; ++i) {
               int j = i1 + rand.nextInt(2);
               int k = i1 + rand.nextInt(2);
               int l = i1 + rand.nextInt(2);
               float f = (float)(j + k + l) * 0.333F + 0.5F;
               Iterator var10 = BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l)).iterator();

               while(var10.hasNext()) {
                  BlockPos blockpos = (BlockPos)var10.next();
                  if (blockpos.distanceSq(position) <= (double)(f * f)) {
                     worldIn.setBlockState(blockpos, this.block.getDefaultState(), 4);
                  }
               }

               position = position.add(-(i1 + 1) + rand.nextInt(2 + i1 * 2), 0 - rand.nextInt(2), -(i1 + 1) + rand.nextInt(2 + i1 * 2));
            }

            return true;
         }

         position = position.down();
      }
   }
}
