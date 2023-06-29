package net.minecraft.block.state;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Iterables;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.Namespaced;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public abstract class BlockStateBase implements IBlockState
{
    private static final Joiner COMMA_JOINER = Joiner.on(',');
    private static final Function < Map.Entry < IProperty<?>, Comparable<? >> , String > MAP_ENTRY_TO_STRING = new Function < Map.Entry < IProperty<?>, Comparable<? >> , String > ()
    {
        @Nullable
        public String apply(@Nullable Map.Entry < IProperty<?>, Comparable<? >> p_apply_1_)
        {
            if (p_apply_1_ == null)
            {
                return "<NULL>";
            }
            else
            {
                IProperty<?> iproperty = p_apply_1_.getKey();
                return iproperty.getName() + "=" + getPropertyName(iproperty, p_apply_1_.getValue());
            }
        }
        private <T extends Comparable<T>> String getPropertyName(IProperty<T> property, Comparable<?> entry)
        {
            return property.getName((T) entry);
        }
    };
    private int blockId = -1;
    private int blockStateId = -1;
    private int metadata = -1;
    private Namespaced blockLocation;

    public int getBlockId()
    {
        if (blockId < 0)
        {
            blockId = Block.getIdFromBlock(getBlock());
        }

        return blockId;
    }

    public int getBlockStateId()
    {
        if (blockStateId < 0)
        {
            blockStateId = Block.getStateId(this);
        }

        return blockStateId;
    }

    public int getMetadata()
    {
        if (metadata < 0)
        {
            metadata = getBlock().getMetaFromState(this);
        }

        return metadata;
    }

    public Namespaced getBlockLocation()
    {
        if (blockLocation == null)
        {
            blockLocation = Block.REGISTRY.getNameForObject(getBlock());
        }

        return blockLocation;
    }

    public ImmutableTable < IProperty<?>, Comparable<?>, IBlockState > getPropertyValueTable()
    {
        return null;
    }

    public <T extends Comparable<T>> IBlockState cycleProperty(IProperty<T> property)
    {
        return withProperty(property, cyclePropertyValue(property.getAllowedValues(), getValue(property)));
    }

    protected static <T> T cyclePropertyValue(Collection<T> values, T currentValue)
    {
        Iterator<T> iterator = values.iterator();

        while (iterator.hasNext())
        {
            if (iterator.next().equals(currentValue))
            {
                if (iterator.hasNext())
                {
                    return iterator.next();
                }

                return values.iterator().next();
            }
        }

        return iterator.next();
    }

    public String toString()
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append(Block.REGISTRY.getNameForObject(getBlock()));

        if (!getProperties().isEmpty())
        {
            stringbuilder.append("[");
            COMMA_JOINER.appendTo(stringbuilder, Iterables.transform(getProperties().entrySet(), MAP_ENTRY_TO_STRING));
            stringbuilder.append("]");
        }

        return stringbuilder.toString();
    }
}
