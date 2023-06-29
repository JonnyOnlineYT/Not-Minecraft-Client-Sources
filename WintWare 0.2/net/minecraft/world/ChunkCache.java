package net.minecraft.world;

import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

public class ChunkCache implements IBlockAccess {
   protected int chunkX;
   protected int chunkZ;
   protected Chunk[][] chunkArray;
   protected boolean hasExtendedLevels;
   protected World worldObj;

   public ChunkCache(World worldIn, BlockPos posFromIn, BlockPos posToIn, int subIn) {
      this.worldObj = worldIn;
      this.chunkX = posFromIn.getX() - subIn >> 4;
      this.chunkZ = posFromIn.getZ() - subIn >> 4;
      int i = posToIn.getX() + subIn >> 4;
      int j = posToIn.getZ() + subIn >> 4;
      this.chunkArray = new Chunk[i - this.chunkX + 1][j - this.chunkZ + 1];
      this.hasExtendedLevels = true;

      int i1;
      int j1;
      for(i1 = this.chunkX; i1 <= i; ++i1) {
         for(j1 = this.chunkZ; j1 <= j; ++j1) {
            this.chunkArray[i1 - this.chunkX][j1 - this.chunkZ] = worldIn.getChunkFromChunkCoords(i1, j1);
         }
      }

      for(i1 = posFromIn.getX() >> 4; i1 <= posToIn.getX() >> 4; ++i1) {
         for(j1 = posFromIn.getZ() >> 4; j1 <= posToIn.getZ() >> 4; ++j1) {
            Chunk chunk = this.chunkArray[i1 - this.chunkX][j1 - this.chunkZ];
            if (chunk != null && !chunk.getAreLevelsEmpty(posFromIn.getY(), posToIn.getY())) {
               this.hasExtendedLevels = false;
            }
         }
      }

   }

   public boolean extendedLevelsInChunkCache() {
      return this.hasExtendedLevels;
   }

   @Nullable
   public TileEntity getTileEntity(BlockPos pos) {
      return this.getTileEntity(pos, Chunk.EnumCreateEntityType.IMMEDIATE);
   }

   @Nullable
   public TileEntity getTileEntity(BlockPos p_190300_1_, Chunk.EnumCreateEntityType p_190300_2_) {
      int i = (p_190300_1_.getX() >> 4) - this.chunkX;
      int j = (p_190300_1_.getZ() >> 4) - this.chunkZ;
      return this.chunkArray[i][j].getTileEntity(p_190300_1_, p_190300_2_);
   }

   public int getCombinedLight(BlockPos pos, int lightValue) {
      int i = this.getLightForExt(EnumSkyBlock.SKY, pos);
      int j = this.getLightForExt(EnumSkyBlock.BLOCK, pos);
      if (j < lightValue) {
         j = lightValue;
      }

      return i << 20 | j << 4;
   }

   public IBlockState getBlockState(BlockPos pos) {
      if (pos.getY() >= 0 && pos.getY() < 256) {
         int i = (pos.getX() >> 4) - this.chunkX;
         int j = (pos.getZ() >> 4) - this.chunkZ;
         if (i >= 0 && i < this.chunkArray.length && j >= 0 && j < this.chunkArray[i].length) {
            Chunk chunk = this.chunkArray[i][j];
            if (chunk != null) {
               return chunk.getBlockState(pos);
            }
         }
      }

      return Blocks.AIR.getDefaultState();
   }

   public Biome getBiome(BlockPos pos) {
      int i = (pos.getX() >> 4) - this.chunkX;
      int j = (pos.getZ() >> 4) - this.chunkZ;
      return this.chunkArray[i][j].getBiome(pos, this.worldObj.getBiomeProvider());
   }

   private int getLightForExt(EnumSkyBlock type, BlockPos pos) {
      if (type == EnumSkyBlock.SKY && !this.worldObj.provider.func_191066_m()) {
         return 0;
      } else if (pos.getY() >= 0 && pos.getY() < 256) {
         int l;
         if (this.getBlockState(pos).useNeighborBrightness()) {
            l = 0;
            EnumFacing[] var9 = EnumFacing.values();
            int var5 = var9.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               EnumFacing enumfacing = var9[var6];
               int k = this.getLightFor(type, pos.offset(enumfacing));
               if (k > l) {
                  l = k;
               }

               if (l >= 15) {
                  return l;
               }
            }

            return l;
         } else {
            l = (pos.getX() >> 4) - this.chunkX;
            int j = (pos.getZ() >> 4) - this.chunkZ;
            return this.chunkArray[l][j].getLightFor(type, pos);
         }
      } else {
         return type.defaultLightValue;
      }
   }

   public boolean isAirBlock(BlockPos pos) {
      return this.getBlockState(pos).getMaterial() == Material.AIR;
   }

   public int getLightFor(EnumSkyBlock p_175628_1_, BlockPos pos) {
      if (pos.getY() >= 0 && pos.getY() < 256) {
         int i = (pos.getX() >> 4) - this.chunkX;
         int j = (pos.getZ() >> 4) - this.chunkZ;
         return this.chunkArray[i][j].getLightFor(p_175628_1_, pos);
      } else {
         return p_175628_1_.defaultLightValue;
      }
   }

   public int getStrongPower(BlockPos pos, EnumFacing direction) {
      return this.getBlockState(pos).getStrongPower(this, pos, direction);
   }

   public WorldType getWorldType() {
      return this.worldObj.getWorldType();
   }
}
