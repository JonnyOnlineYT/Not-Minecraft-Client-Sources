package net.minecraft.block.state.pattern;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;

import javax.annotation.Nullable;
import java.util.Map;

public class BlockStateMatcher implements Predicate<IBlockState>
{
    public static final Predicate<IBlockState> ANY = new Predicate<IBlockState>()
    {
        public boolean apply(@Nullable IBlockState p_apply_1_)
        {
            return true;
        }
    };
    private final BlockStateContainer blockstate;
    private final Map < IProperty<?>, Predicate<? >> propertyPredicates = Maps.newHashMap();

    private BlockStateMatcher(BlockStateContainer blockStateIn)
    {
        blockstate = blockStateIn;
    }

    public static BlockStateMatcher forBlock(Block blockIn)
    {
        return new BlockStateMatcher(blockIn.getBlockState());
    }

    public boolean apply(@Nullable IBlockState p_apply_1_)
    {
        if (p_apply_1_ != null && p_apply_1_.getBlock().equals(blockstate.getBlock()))
        {
            if (propertyPredicates.isEmpty())
            {
                return true;
            }
            else
            {
                for (Map.Entry < IProperty<?>, Predicate<? >> entry : propertyPredicates.entrySet())
                {
                    if (!matches(p_apply_1_, (IProperty)entry.getKey(), (Predicate)entry.getValue()))
                    {
                        return false;
                    }
                }

                return true;
            }
        }
        else
        {
            return false;
        }
    }

    protected <T extends Comparable<T>> boolean matches(IBlockState blockState, IProperty<T> property, Predicate<T> predicate)
    {
        return predicate.apply(blockState.getValue(property));
    }

    public <V extends Comparable<V>> BlockStateMatcher where(IProperty<V> property, Predicate <? extends V > is)
    {
        if (!blockstate.getProperties().contains(property))
        {
            throw new IllegalArgumentException(blockstate + " cannot support property " + property);
        }
        else
        {
            propertyPredicates.put(property, is);
            return this;
        }
    }
}
