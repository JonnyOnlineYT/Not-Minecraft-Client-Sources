package net.minecraft.block.state.pattern;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.state.BlockWorldState;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

public class FactoryBlockPattern
{
    private static final Joiner COMMA_JOIN = Joiner.on(",");
    private final List<String[]> depth = Lists.newArrayList();
    private final Map<Character, Predicate<BlockWorldState>> symbolMap = Maps.newHashMap();
    private int aisleHeight;
    private int rowWidth;

    private FactoryBlockPattern()
    {
        symbolMap.put(' ', Predicates.alwaysTrue());
    }

    /**
     * Adds a single aisle to this pattern, going in the z axis. (so multiple calls to this will increase the z-size by
     * 1)
     */
    public FactoryBlockPattern aisle(String... aisle)
    {
        if (!ArrayUtils.isEmpty(aisle) && !StringUtils.isEmpty(aisle[0]))
        {
            if (depth.isEmpty())
            {
                aisleHeight = aisle.length;
                rowWidth = aisle[0].length();
            }

            if (aisle.length != aisleHeight)
            {
                throw new IllegalArgumentException("Expected aisle with height of " + aisleHeight + ", but was given one with a height of " + aisle.length + ")");
            }
            else
            {
                for (String s : aisle)
                {
                    if (s.length() != rowWidth)
                    {
                        throw new IllegalArgumentException("Not all rows in the given aisle are the correct width (expected " + rowWidth + ", found one with " + s.length() + ")");
                    }

                    for (char c0 : s.toCharArray())
                    {
                        if (!symbolMap.containsKey(Character.valueOf(c0)))
                        {
                            symbolMap.put(Character.valueOf(c0), null);
                        }
                    }
                }

                depth.add(aisle);
                return this;
            }
        }
        else
        {
            throw new IllegalArgumentException("Empty pattern for aisle");
        }
    }

    public static FactoryBlockPattern start()
    {
        return new FactoryBlockPattern();
    }

    public FactoryBlockPattern where(char symbol, Predicate<BlockWorldState> blockMatcher)
    {
        symbolMap.put(Character.valueOf(symbol), blockMatcher);
        return this;
    }

    public BlockPattern build()
    {
        return new BlockPattern(makePredicateArray());
    }

    private Predicate<BlockWorldState>[][][] makePredicateArray()
    {
        checkMissingPredicates();
        Predicate<BlockWorldState>[][][] predicate = (Predicate[][][]) Array.newInstance(Predicate.class, depth.size(), aisleHeight, rowWidth);

        for (int i = 0; i < depth.size(); ++i)
        {
            for (int j = 0; j < aisleHeight; ++j)
            {
                for (int k = 0; k < rowWidth; ++k)
                {
                    predicate[i][j][k] = symbolMap.get(Character.valueOf(((String[]) depth.get(i))[j].charAt(k)));
                }
            }
        }

        return predicate;
    }

    private void checkMissingPredicates()
    {
        List<Character> list = Lists.newArrayList();

        for (Map.Entry<Character, Predicate<BlockWorldState>> entry : symbolMap.entrySet())
        {
            if (entry.getValue() == null)
            {
                list.add(entry.getKey());
            }
        }

        if (!list.isEmpty())
        {
            throw new IllegalStateException("Predicates for character(s) " + COMMA_JOIN.join(list) + " are missing");
        }
    }
}
