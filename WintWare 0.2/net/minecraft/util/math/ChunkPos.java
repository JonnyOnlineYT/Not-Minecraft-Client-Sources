package net.minecraft.util.math;

import net.minecraft.entity.Entity;

public class ChunkPos {
   public final int chunkXPos;
   public final int chunkZPos;
   private int cachedHashCode = 0;

   public ChunkPos(int x, int z) {
      this.chunkXPos = x;
      this.chunkZPos = z;
   }

   public ChunkPos(BlockPos pos) {
      this.chunkXPos = pos.getX() >> 4;
      this.chunkZPos = pos.getZ() >> 4;
   }

   public static long asLong(int x, int z) {
      return (long)x & 4294967295L | ((long)z & 4294967295L) << 32;
   }

   public int hashCode() {
      if (this.cachedHashCode != 0) {
         return this.cachedHashCode;
      } else {
         int i = 1664525 * this.chunkXPos + 1013904223;
         int j = 1664525 * (this.chunkZPos ^ -559038737) + 1013904223;
         this.cachedHashCode = i ^ j;
         return this.cachedHashCode;
      }
   }

   public boolean equals(Object p_equals_1_) {
      if (this == p_equals_1_) {
         return true;
      } else if (!(p_equals_1_ instanceof ChunkPos)) {
         return false;
      } else {
         ChunkPos chunkpos = (ChunkPos)p_equals_1_;
         return this.chunkXPos == chunkpos.chunkXPos && this.chunkZPos == chunkpos.chunkZPos;
      }
   }

   public double getDistanceSq(Entity entityIn) {
      double d0 = (double)(this.chunkXPos * 16 + 8);
      double d1 = (double)(this.chunkZPos * 16 + 8);
      double d2 = d0 - entityIn.posX;
      double d3 = d1 - entityIn.posZ;
      return d2 * d2 + d3 * d3;
   }

   public int getXStart() {
      return this.chunkXPos << 4;
   }

   public int getZStart() {
      return this.chunkZPos << 4;
   }

   public int getXEnd() {
      return (this.chunkXPos << 4) + 15;
   }

   public int getZEnd() {
      return (this.chunkZPos << 4) + 15;
   }

   public BlockPos getBlock(int x, int y, int z) {
      return new BlockPos((this.chunkXPos << 4) + x, y, (this.chunkZPos << 4) + z);
   }

   public String toString() {
      return "[" + this.chunkXPos + ", " + this.chunkZPos + "]";
   }
}
