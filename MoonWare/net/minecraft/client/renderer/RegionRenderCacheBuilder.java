package net.minecraft.client.renderer;

import net.minecraft.util.BlockRenderLayer;

public class RegionRenderCacheBuilder
{
    private final BufferBuilder[] worldRenderers = new BufferBuilder[BlockRenderLayer.values().length];

    public RegionRenderCacheBuilder()
    {
        worldRenderers[BlockRenderLayer.SOLID.ordinal()] = new BufferBuilder(2097152);
        worldRenderers[BlockRenderLayer.CUTOUT.ordinal()] = new BufferBuilder(131072);
        worldRenderers[BlockRenderLayer.CUTOUT_MIPPED.ordinal()] = new BufferBuilder(131072);
        worldRenderers[BlockRenderLayer.TRANSLUCENT.ordinal()] = new BufferBuilder(262144);
    }

    public BufferBuilder getWorldRendererByLayer(BlockRenderLayer layer)
    {
        return worldRenderers[layer.ordinal()];
    }

    public BufferBuilder getWorldRendererByLayerId(int id)
    {
        return worldRenderers[id];
    }
}
