package net.minecraft.client.renderer.block.statemap;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelNamespaced;
import net.minecraft.util.Namespaced;

public class BlockStateMapper
{
    private final Map<Block, IStateMapper> blockStateMap = Maps.newIdentityHashMap();
    private final Set<Block> setBuiltInBlocks = Sets.newIdentityHashSet();

    public void registerBlockStateMapper(Block blockIn, IStateMapper stateMapper)
    {
        blockStateMap.put(blockIn, stateMapper);
    }

    public void registerBuiltInBlocks(Block... blockIn)
    {
        Collections.addAll(setBuiltInBlocks, blockIn);
    }

    public Map<IBlockState, ModelNamespaced> putAllStateModelLocations()
    {
        Map<IBlockState, ModelNamespaced> map = Maps.newIdentityHashMap();

        for (Block block : Block.REGISTRY)
        {
            map.putAll(getVariants(block));
        }

        return map;
    }

    public Set<Namespaced> getBlockstateLocations(Block blockIn)
    {
        if (setBuiltInBlocks.contains(blockIn))
        {
            return Collections.emptySet();
        }
        else
        {
            IStateMapper istatemapper = blockStateMap.get(blockIn);

            if (istatemapper == null)
            {
                return Collections.singleton(Block.REGISTRY.getNameForObject(blockIn));
            }
            else
            {
                Set<Namespaced> set = Sets.newHashSet();

                for (ModelNamespaced modelresourcelocation : istatemapper.putStateModelLocations(blockIn).values())
                {
                    set.add(new Namespaced(modelresourcelocation.getNamespace(), modelresourcelocation.getPath()));
                }

                return set;
            }
        }
    }

    public Map<IBlockState, ModelNamespaced> getVariants(Block blockIn)
    {
        return setBuiltInBlocks.contains(blockIn) ? Collections.emptyMap() : MoreObjects.firstNonNull(blockStateMap.get(blockIn), new DefaultStateMapper()).putStateModelLocations(blockIn);
    }
}
