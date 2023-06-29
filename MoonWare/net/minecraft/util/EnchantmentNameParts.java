package net.minecraft.util;

import com.google.common.base.Joiner;
import net.minecraft.client.gui.Font;

import java.util.List;
import java.util.Random;

public class EnchantmentNameParts
{
    private static final EnchantmentNameParts INSTANCE = new EnchantmentNameParts();
    private final Random rand = new Random();
    private final String[] namePartsArray = "the elder scrolls klaatu berata niktu xyzzy bless curse light darkness fire air earth water hot dry cold wet ignite snuff embiggen twist shorten stretch fiddle destroy imbue galvanize enchant free limited range of towards inside sphere cube self other ball mental physical grow shrink demon elemental spirit animal creature beast humanoid undead fresh stale phnglui mglwnafh cthulhu rlyeh wgahnagl fhtagnbaguette".split(" ");

    public static EnchantmentNameParts getInstance()
    {
        return INSTANCE;
    }

    /**
     * Randomly generates a new name built up of 3 or 4 randomly selected words.
     */
    public String generateNewRandomName(Font p_148334_1_, int p_148334_2_)
    {
        int i = rand.nextInt(2) + 3;
        String s = "";

        for (int j = 0; j < i; ++j)
        {
            if (j > 0)
            {
                s = s + " ";
            }

            s = s + namePartsArray[rand.nextInt(namePartsArray.length)];
        }

        List<String> list = p_148334_1_.split(s, p_148334_2_);
        return Joiner.on(' ').join(list.size() >= 2 ? list.subList(0, 2) : list);
    }

    /**
     * Resets the underlying random number generator using a given seed.
     */
    public void reseedRandomGenerator(long seed)
    {
        rand.setSeed(seed);
    }
}
