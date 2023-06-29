package net.minecraft.client.renderer.block.statemap;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelNamespaced;

public class DefaultStateMapper extends StateMapperBase
{
    protected ModelNamespaced getModelResourceLocation(IBlockState state)
    {
        return new ModelNamespaced(Block.REGISTRY.getNameForObject(state.getBlock()), getPropertyString(state.getProperties()));
    }
}
