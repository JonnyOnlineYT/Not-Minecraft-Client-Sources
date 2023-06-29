package net.minecraft.client.renderer.block.statemap;

import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelNamespaced;

public interface IStateMapper
{
    Map<IBlockState, ModelNamespaced> putStateModelLocations(Block blockIn);
}
