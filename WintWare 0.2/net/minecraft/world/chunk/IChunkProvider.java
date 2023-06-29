package net.minecraft.world.chunk;

import javax.annotation.Nullable;

public interface IChunkProvider {
   @Nullable
   Chunk getLoadedChunk(int var1, int var2);

   Chunk provideChunk(int var1, int var2);

   boolean unloadQueuedChunks();

   String makeString();

   boolean func_191062_e(int var1, int var2);
}
