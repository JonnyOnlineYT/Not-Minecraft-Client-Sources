package net.minecraft.item;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.Formatting;

public enum EnumDyeColor implements IStringSerializable
{
    WHITE(0, 15, "white", "white", 16383998, Formatting.WHITE),
    ORANGE(1, 14, "orange", "orange", 16351261, Formatting.GOLD),
    MAGENTA(2, 13, "magenta", "magenta", 13061821, Formatting.AQUA),
    LIGHT_BLUE(3, 12, "light_blue", "lightBlue", 3847130, Formatting.BLUE),
    YELLOW(4, 11, "yellow", "yellow", 16701501, Formatting.YELLOW),
    LIME(5, 10, "lime", "lime", 8439583, Formatting.GREEN),
    PINK(6, 9, "pink", "pink", 15961002, Formatting.LIGHT_PURPLE),
    GRAY(7, 8, "gray", "gray", 4673362, Formatting.DARK_GRAY),
    SILVER(8, 7, "silver", "silver", 10329495, Formatting.GRAY),
    CYAN(9, 6, "cyan", "cyan", 1481884, Formatting.DARK_AQUA),
    PURPLE(10, 5, "purple", "purple", 8991416, Formatting.DARK_PURPLE),
    BLUE(11, 4, "blue", "blue", 3949738, Formatting.DARK_BLUE),
    BROWN(12, 3, "brown", "brown", 8606770, Formatting.GOLD),
    GREEN(13, 2, "green", "green", 6192150, Formatting.DARK_GREEN),
    RED(14, 1, "red", "red", 11546150, Formatting.DARK_RED),
    BLACK(15, 0, "black", "black", 1908001, Formatting.BLACK);

    private static final EnumDyeColor[] META_LOOKUP = new EnumDyeColor[values().length];
    private static final EnumDyeColor[] DYE_DMG_LOOKUP = new EnumDyeColor[values().length];
    private final int meta;
    private final int dyeDamage;
    private final String name;
    private final String unlocalizedName;
    private final int field_193351_w;
    private final float[] field_193352_x;
    private final Formatting chatColor;

    EnumDyeColor(int p_i47505_3_, int p_i47505_4_, String p_i47505_5_, String p_i47505_6_, int p_i47505_7_, Formatting p_i47505_8_)
    {
        meta = p_i47505_3_;
        dyeDamage = p_i47505_4_;
        name = p_i47505_5_;
        unlocalizedName = p_i47505_6_;
        field_193351_w = p_i47505_7_;
        chatColor = p_i47505_8_;
        int i = (p_i47505_7_ & 16711680) >> 16;
        int j = (p_i47505_7_ & 65280) >> 8;
        int k = (p_i47505_7_ & 255) >> 0;
        field_193352_x = new float[] {(float)i / 255.0F, (float)j / 255.0F, (float)k / 255.0F};
    }

    public int getMetadata()
    {
        return meta;
    }

    public int getDyeDamage()
    {
        return dyeDamage;
    }

    public String func_192396_c()
    {
        return name;
    }

    public String getUnlocalizedName()
    {
        return unlocalizedName;
    }

    public int func_193350_e()
    {
        return field_193351_w;
    }

    public float[] func_193349_f()
    {
        return field_193352_x;
    }

    public static EnumDyeColor byDyeDamage(int damage)
    {
        if (damage < 0 || damage >= DYE_DMG_LOOKUP.length)
        {
            damage = 0;
        }

        return DYE_DMG_LOOKUP[damage];
    }

    public static EnumDyeColor byMetadata(int meta)
    {
        if (meta < 0 || meta >= META_LOOKUP.length)
        {
            meta = 0;
        }

        return META_LOOKUP[meta];
    }

    public String toString()
    {
        return unlocalizedName;
    }

    public String getName()
    {
        return name;
    }

    static {
        for (EnumDyeColor enumdyecolor : values())
        {
            META_LOOKUP[enumdyecolor.getMetadata()] = enumdyecolor;
            DYE_DMG_LOOKUP[enumdyecolor.getDyeDamage()] = enumdyecolor;
        }
    }
}
