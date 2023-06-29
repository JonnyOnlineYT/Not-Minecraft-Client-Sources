package optifine;

import java.util.Arrays;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

public class ChunkCacheOF implements IBlockAccess
{
    private ChunkCache chunkCache;
    private int posX;
    private int posY;
    private int posZ;
    private int[] combinedLights;
    private IBlockState[] blockStates;
    private static ArrayCache cacheCombinedLights = new ArrayCache(Integer.TYPE, 16);
    private static ArrayCache cacheBlockStates = new ArrayCache(IBlockState.class, 16);
    private static final int ARRAY_SIZE = 8000;

    public ChunkCacheOF(ChunkCache p_i22_1_, BlockPos p_i22_2_, int p_i22_3_)
    {
        chunkCache = p_i22_1_;
        posX = p_i22_2_.getX() - p_i22_3_;
        posY = p_i22_2_.getY() - p_i22_3_;
        posZ = p_i22_2_.getZ() - p_i22_3_;
    }

    public int getCombinedLight(BlockPos pos, int lightValue)
    {
        if (combinedLights == null)
        {
            int k = chunkCache.getCombinedLight(pos, lightValue);

            if (Config.isDynamicLights() && !getBlockState(pos).isOpaqueCube())
            {
                k = DynamicLights.getCombinedLight(pos, k);
            }

            return k;
        }
        else
        {
            int i = getPositionIndex(pos);

            if (i >= 0 && i < combinedLights.length)
            {
                int j = combinedLights[i];

                if (j == -1)
                {
                    j = chunkCache.getCombinedLight(pos, lightValue);

                    if (Config.isDynamicLights() && !getBlockState(pos).isOpaqueCube())
                    {
                        j = DynamicLights.getCombinedLight(pos, j);
                    }

                    combinedLights[i] = j;
                }

                return j;
            }
            else
            {
                return chunkCache.getCombinedLight(pos, lightValue);
            }
        }
    }

    public IBlockState getBlockState(BlockPos pos)
    {
        if (blockStates == null)
        {
            return chunkCache.getBlockState(pos);
        }
        else
        {
            int i = getPositionIndex(pos);

            if (i >= 0 && i < blockStates.length)
            {
                IBlockState iblockstate = blockStates[i];

                if (iblockstate == null)
                {
                    iblockstate = chunkCache.getBlockState(pos);
                    blockStates[i] = iblockstate;
                }

                return iblockstate;
            }
            else
            {
                return chunkCache.getBlockState(pos);
            }
        }
    }

    private int getPositionIndex(BlockPos p_getPositionIndex_1_)
    {
        int i = p_getPositionIndex_1_.getX() - posX;
        int j = p_getPositionIndex_1_.getY() - posY;
        int k = p_getPositionIndex_1_.getZ() - posZ;
        return i * 400 + k * 20 + j;
    }

    public void renderStart()
    {
        if (combinedLights == null)
        {
            combinedLights = (int[]) cacheCombinedLights.allocate(8000);
        }

        Arrays.fill(combinedLights, -1);

        if (blockStates == null)
        {
            blockStates = (IBlockState[]) cacheBlockStates.allocate(8000);
        }

        Arrays.fill(blockStates, null);
    }

    public void renderFinish()
    {
        cacheCombinedLights.free(combinedLights);
        combinedLights = null;
        cacheBlockStates.free(blockStates);
        blockStates = null;
    }

    public boolean isEmpty()
    {
        return chunkCache.extendedLevelsInChunkCache();
    }

    public Biome getBiome(BlockPos pos)
    {
        return chunkCache.getBiome(pos);
    }

    public int getStrongPower(BlockPos pos, EnumFacing direction)
    {
        return chunkCache.getStrongPower(pos, direction);
    }

    public TileEntity getTileEntity(BlockPos pos)
    {
        return chunkCache.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
    }

    public TileEntity getTileEntity(BlockPos p_getTileEntity_1_, Chunk.EnumCreateEntityType p_getTileEntity_2_)
    {
        return chunkCache.getTileEntity(p_getTileEntity_1_, p_getTileEntity_2_);
    }

    public WorldType getWorldType()
    {
        return chunkCache.getWorldType();
    }

    /**
     * Checks to see if an air block exists at the provided location. Note that this only checks to see if the blocks
     * material is set to air, meaning it is possible for non-vanilla blocks to still pass this check.
     */
    public boolean isAirBlock(BlockPos pos)
    {
        return chunkCache.isAirBlock(pos);
    }

    public boolean isSideSolid(BlockPos p_isSideSolid_1_, EnumFacing p_isSideSolid_2_, boolean p_isSideSolid_3_)
    {
        return Reflector.callBoolean(chunkCache, Reflector.ForgeChunkCache_isSideSolid, p_isSideSolid_1_, p_isSideSolid_2_, p_isSideSolid_3_);
    }
}
