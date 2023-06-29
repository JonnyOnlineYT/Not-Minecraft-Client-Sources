package net.minecraft.world;

public class WorldProviderSurface extends WorldProvider {
   public DimensionType getDimensionType() {
      return DimensionType.OVERWORLD;
   }

   public boolean canDropChunk(int x, int z) {
      return !this.worldObj.isSpawnChunk(x, z);
   }
}
