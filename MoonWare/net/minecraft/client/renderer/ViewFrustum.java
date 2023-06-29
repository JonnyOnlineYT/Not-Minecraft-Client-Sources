package net.minecraft.client.renderer;

import javax.annotation.Nullable;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ViewFrustum
{
    protected final RenderGlobal renderGlobal;
    protected final World world;
    protected int countChunksY;
    protected int countChunksX;
    protected int countChunksZ;
    public RenderChunk[] renderChunks;

    public ViewFrustum(World worldIn, int renderDistanceChunks, RenderGlobal renderGlobalIn, IRenderChunkFactory renderChunkFactory)
    {
        renderGlobal = renderGlobalIn;
        world = worldIn;
        setCountChunksXYZ(renderDistanceChunks);
        createRenderChunks(renderChunkFactory);
    }

    protected void createRenderChunks(IRenderChunkFactory renderChunkFactory)
    {
        int i = countChunksX * countChunksY * countChunksZ;
        renderChunks = new RenderChunk[i];
        int j = 0;

        for (int k = 0; k < countChunksX; ++k)
        {
            for (int l = 0; l < countChunksY; ++l)
            {
                for (int i1 = 0; i1 < countChunksZ; ++i1)
                {
                    int j1 = (i1 * countChunksY + l) * countChunksX + k;
                    renderChunks[j1] = renderChunkFactory.create(world, renderGlobal, j++);
                    renderChunks[j1].setPosition(k * 16, l * 16, i1 * 16);
                }
            }
        }
    }

    public void deleteGlResources()
    {
        for (RenderChunk renderchunk : renderChunks)
        {
            renderchunk.deleteGlResources();
        }
    }

    protected void setCountChunksXYZ(int renderDistanceChunks)
    {
        int i = renderDistanceChunks * 2 + 1;
        countChunksX = i;
        countChunksY = 16;
        countChunksZ = i;
    }

    public void updateChunkPositions(double viewEntityX, double viewEntityZ)
    {
        int i = MathHelper.floor(viewEntityX) - 8;
        int j = MathHelper.floor(viewEntityZ) - 8;
        int k = countChunksX * 16;

        for (int l = 0; l < countChunksX; ++l)
        {
            int i1 = getBaseCoordinate(i, k, l);

            for (int j1 = 0; j1 < countChunksZ; ++j1)
            {
                int k1 = getBaseCoordinate(j, k, j1);

                for (int l1 = 0; l1 < countChunksY; ++l1)
                {
                    int i2 = l1 * 16;
                    RenderChunk renderchunk = renderChunks[(j1 * countChunksY + l1) * countChunksX + l];
                    renderchunk.setPosition(i1, i2, k1);
                }
            }
        }
    }

    private int getBaseCoordinate(int p_178157_1_, int p_178157_2_, int p_178157_3_)
    {
        int i = p_178157_3_ * 16;
        int j = i - p_178157_1_ + p_178157_2_ / 2;

        if (j < 0)
        {
            j -= p_178157_2_ - 1;
        }

        return i - j / p_178157_2_ * p_178157_2_;
    }

    public void markBlocksForUpdate(int p_187474_1_, int p_187474_2_, int p_187474_3_, int p_187474_4_, int p_187474_5_, int p_187474_6_, boolean p_187474_7_)
    {
        int i = MathHelper.intFloorDiv(p_187474_1_, 16);
        int j = MathHelper.intFloorDiv(p_187474_2_, 16);
        int k = MathHelper.intFloorDiv(p_187474_3_, 16);
        int l = MathHelper.intFloorDiv(p_187474_4_, 16);
        int i1 = MathHelper.intFloorDiv(p_187474_5_, 16);
        int j1 = MathHelper.intFloorDiv(p_187474_6_, 16);

        for (int k1 = i; k1 <= l; ++k1)
        {
            int l1 = k1 % countChunksX;

            if (l1 < 0)
            {
                l1 += countChunksX;
            }

            for (int i2 = j; i2 <= i1; ++i2)
            {
                int j2 = i2 % countChunksY;

                if (j2 < 0)
                {
                    j2 += countChunksY;
                }

                for (int k2 = k; k2 <= j1; ++k2)
                {
                    int l2 = k2 % countChunksZ;

                    if (l2 < 0)
                    {
                        l2 += countChunksZ;
                    }

                    int i3 = (l2 * countChunksY + j2) * countChunksX + l1;
                    RenderChunk renderchunk = renderChunks[i3];
                    renderchunk.setNeedsUpdate(p_187474_7_);
                }
            }
        }
    }

    @Nullable
    public RenderChunk getRenderChunk(BlockPos pos)
    {
        int i = pos.getX() >> 4;
        int j = pos.getY() >> 4;
        int k = pos.getZ() >> 4;

        if (j >= 0 && j < countChunksY)
        {
            i = i % countChunksX;

            if (i < 0)
            {
                i += countChunksX;
            }

            k = k % countChunksZ;

            if (k < 0)
            {
                k += countChunksZ;
            }

            int l = (k * countChunksY + j) * countChunksX + i;
            return renderChunks[l];
        }
        else
        {
            return null;
        }
    }
}
