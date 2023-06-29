package net.minecraft.client.renderer.block.model.multipart;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;

public class ConditionPropertyValue implements ICondition
{
    private static final Splitter SPLITTER = Splitter.on('|').omitEmptyStrings();
    private final String key;
    private final String value;

    public ConditionPropertyValue(String keyIn, String valueIn)
    {
        key = keyIn;
        value = valueIn;
    }

    public Predicate<IBlockState> getPredicate(BlockStateContainer blockState)
    {
        IProperty<?> iproperty = blockState.getProperty(key);

        if (iproperty == null)
        {
            throw new RuntimeException(this + ": Definition: " + blockState + " has no property: " + key);
        }
        else
        {
            String s = value;
            boolean flag = !s.isEmpty() && s.charAt(0) == '!';

            if (flag)
            {
                s = s.substring(1);
            }

            List<String> list = SPLITTER.splitToList(s);

            if (list.isEmpty())
            {
                throw new RuntimeException(this + ": has an empty value: " + value);
            }
            else
            {
                Predicate<IBlockState> predicate;

                if (list.size() == 1)
                {
                    predicate = makePredicate(iproperty, s);
                }
                else
                {
                    predicate = Predicates.or(Iterables.transform(list, new Function<String, Predicate<IBlockState>>()
                    {
                        @Nullable
                        public Predicate<IBlockState> apply(@Nullable String p_apply_1_)
                        {
                            return makePredicate(iproperty, p_apply_1_);
                        }
                    }));
                }

                return flag ? Predicates.not(predicate) : predicate;
            }
        }
    }

    private Predicate<IBlockState> makePredicate(IProperty<?> property, String valueIn)
    {
        Optional<?> optional = property.parseValue(valueIn);

        if (!optional.isPresent())
        {
            throw new RuntimeException(this + ": has an unknown value: " + value);
        }
        else
        {
            return new Predicate<IBlockState>()
            {
                public boolean apply(@Nullable IBlockState p_apply_1_)
                {
                    return p_apply_1_ != null && p_apply_1_.getValue(property).equals(optional.get());
                }
            };
        }
    }

    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("key", key).add("value", value).toString();
    }
}
