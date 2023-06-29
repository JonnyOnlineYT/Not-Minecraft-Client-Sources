package net.minecraft.util;

import io.netty.util.ResourceLeakDetector;

public class ChatAllowedCharacters
{
    public static final ResourceLeakDetector.Level NETTY_LEAK_DETECTION = ResourceLeakDetector.Level.DISABLED;
    public static final char[] ILLEGAL_STRUCTURE_CHARACTERS = {'.', '\n', '\r', '\t', '\u0000', '\f', '`', '?', '*', '\\', '<', '>', '|', '"'};

    /**
     * Array of the special characters that are allowed in any text drawing of Minecraft.
     */
    public static final char[] ILLEGAL_FILE_CHARACTERS = {'/', '\n', '\r', '\t', '\u0000', '\f', '`', '?', '*', '\\', '<', '>', '|', '"', ':'};

    public static boolean isAllowedCharacter(char character)
    {
        return character != 167 && character >= ' ' && character != 127;
    }

    /**
     * Filter string by only keeping those characters for which isAllowedCharacter() returns true.
     */
    public static String filterAllowedCharacters(String input)
    {
        StringBuilder stringbuilder = new StringBuilder();

        for (char c0 : input.toCharArray())
        {
            if (isAllowedCharacter(c0))
            {
                stringbuilder.append(c0);
            }
        }

        return stringbuilder.toString();
    }

    static
    {
        ResourceLeakDetector.setLevel(NETTY_LEAK_DETECTION);
    }
}
