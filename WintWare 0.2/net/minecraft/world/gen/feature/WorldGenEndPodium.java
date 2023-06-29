package net.minecraft.world.gen.feature;

import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.BlockTorch;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenEndPodium extends WorldGenerator {
   public static final BlockPos END_PODIUM_LOCATION;
   public static final BlockPos END_PODIUM_CHUNK_POS;
   private final boolean activePortal;

   public WorldGenEndPodium(boolean activePortalIn) {
      this.activePortal = activePortalIn;
   }

   public boolean generate(World worldIn, Random rand, BlockPos position) {
      Iterator var4 = BlockPos.getAllInBoxMutable(new BlockPos(position.getX() - 4, position.getY() - 1, position.getZ() - 4), new BlockPos(position.getX() + 4, position.getY() + 32, position.getZ() + 4)).iterator();

      while(var4.hasNext()) {
         BlockPos.MutableBlockPos blockpos$mutableblockpos = (BlockPos.MutableBlockPos)var4.next();
         double d0 = blockpos$mutableblockpos.getDistance(position.getX(), blockpos$mutableblockpos.getY(), position.getZ());
         if (d0 <= 3.5D) {
            if (blockpos$mutableblockpos.getY() < position.getY()) {
               if (d0 <= 2.5D) {
                  this.setBlockAndNotifyAdequately(worldIn, blockpos$mutableblockpos, Blocks.BEDROCK.getDefaultState());
               } else if (blockpos$mutableblockpos.getY() < position.getY()) {
                  this.setBlockAndNotifyAdequately(worldIn, blockpos$mutableblockpos, Blocks.END_STONE.getDefaultState());
               }
            } else if (blockpos$mutableblockpos.getY() > position.getY()) {
               this.setBlockAndNotifyAdequately(worldIn, blockpos$mutableblockpos, Blocks.AIR.getDefaultState());
            } else if (d0 > 2.5D) {
               this.setBlockAndNotifyAdequately(worldIn, blockpos$mutableblockpos, Blocks.BEDROCK.getDefaultState());
            } else if (this.activePortal) {
               this.setBlockAndNotifyAdequately(worldIn, new BlockPos(blockpos$mutableblockpos), Blocks.END_PORTAL.getDefaultState());
            } else {
               this.setBlockAndNotifyAdequately(worldIn, new BlockPos(blockpos$mutableblockpos), Blocks.AIR.getDefaultState());
            }
         }
      }

      for(int i = 0; i < 4; ++i) {
         this.setBlockAndNotifyAdequately(worldIn, position.up(i), Blocks.BEDROCK.getDefaultState());
      }

      BlockPos blockpos = position.up(2);
      Iterator var10 = EnumFacing.Plane.HORIZONTAL.iterator();

      while(var10.hasNext()) {
         EnumFacing enumfacing = (EnumFacing)var10.next();
         this.setBlockAndNotifyAdequately(worldIn, blockpos.offset(enumfacing), Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, enumfacing));
      }

      return true;
   }

   static {
      END_PODIUM_LOCATION = BlockPos.ORIGIN;
      END_PODIUM_CHUNK_POS = new BlockPos(END_PODIUM_LOCATION.getX() - 4 & -16, 0, END_PODIUM_LOCATION.getZ() - 4 & -16);
   }
}
