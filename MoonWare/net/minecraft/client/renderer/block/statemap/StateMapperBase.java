package net.minecraft.client.renderer.block.statemap;

import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelNamespaced;

public abstract class StateMapperBase implements IStateMapper
{
    protected Map<IBlockState, ModelNamespaced> mapStateModelLocations = Maps.newLinkedHashMap();

    public String getPropertyString(Map < IProperty<?>, Comparable<? >> values)
    {
        StringBuilder stringbuilder = new StringBuilder();

        for (Map.Entry < IProperty<?>, Comparable<? >> entry : values.entrySet())
        {
            if (stringbuilder.length() != 0)
            {
                stringbuilder.append(",");
            }

            IProperty<?> iproperty = entry.getKey();
            stringbuilder.append(iproperty.getName());
            stringbuilder.append("=");
            stringbuilder.append(getPropertyName(iproperty, entry.getValue()));
        }

        if (stringbuilder.length() == 0)
        {
            stringbuilder.append("normal");
        }

        return stringbuilder.toString();
    }

    private <T extends Comparable<T>> String getPropertyName(IProperty<T> property, Comparable<?> value)
    {
        return property.getName((T)value);
    }

    public Map<IBlockState, ModelNamespaced> putStateModelLocations(Block blockIn)
    {
        UnmodifiableIterator unmodifiableiterator = blockIn.getBlockState().getValidStates().iterator();

        while (unmodifiableiterator.hasNext())
        {
            IBlockState iblockstate = (IBlockState)unmodifiableiterator.next();
            mapStateModelLocations.put(iblockstate, getModelResourceLocation(iblockstate));
        }

        return mapStateModelLocations;
    }

    protected abstract ModelNamespaced getModelResourceLocation(IBlockState state);
}
