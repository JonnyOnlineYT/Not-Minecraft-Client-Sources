package net.minecraft.client.renderer.block.statemap;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelNamespaced;
import net.minecraft.util.Namespaced;

public class StateMap extends StateMapperBase
{
    private final IProperty<?> name;
    private final String suffix;
    private final List < IProperty<? >> ignored;

    private StateMap(@Nullable IProperty<?> name, @Nullable String suffix, List < IProperty<? >> ignored)
    {
        this.name = name;
        this.suffix = suffix;
        this.ignored = ignored;
    }

    protected ModelNamespaced getModelResourceLocation(IBlockState state)
    {
        Map < IProperty<?>, Comparable<? >> map = Maps.newLinkedHashMap(state.getProperties());
        String s;

        if (name == null)
        {
            s = Block.REGISTRY.getNameForObject(state.getBlock()).toString();
        }
        else
        {
            s = removeName(name, map);
        }

        if (suffix != null)
        {
            s = s + suffix;
        }

        for (IProperty<?> iproperty : ignored)
        {
            map.remove(iproperty);
        }

        return new ModelNamespaced(s, getPropertyString(map));
    }

    private <T extends Comparable<T>> String removeName(IProperty<T> p_187490_1_, Map < IProperty<?>, Comparable<? >> p_187490_2_)
    {
        return p_187490_1_.getName((T)p_187490_2_.remove(name));
    }

    public static class Builder
    {
        private IProperty<?> name;
        private String suffix;
        private final List < IProperty<? >> ignored = Lists.newArrayList();

        public StateMap.Builder withName(IProperty<?> builderPropertyIn)
        {
            name = builderPropertyIn;
            return this;
        }

        public StateMap.Builder withSuffix(String builderSuffixIn)
        {
            suffix = builderSuffixIn;
            return this;
        }

        public StateMap.Builder ignore(IProperty<?>... p_178442_1_)
        {
            Collections.addAll(ignored, p_178442_1_);
            return this;
        }

        public StateMap build()
        {
            return new StateMap(name, suffix, ignored);
        }
    }
}
