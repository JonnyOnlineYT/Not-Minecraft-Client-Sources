package net.minecraft.item;

import net.minecraft.util.text.Formatting;

public enum EnumRarity
{
    COMMON(Formatting.WHITE, "Common"),
    UNCOMMON(Formatting.YELLOW, "Uncommon"),
    RARE(Formatting.AQUA, "Rare"),
    EPIC(Formatting.LIGHT_PURPLE, "Epic");

    /**
     * A decimal representation of the hex color codes of a the color assigned to this rarity type. (13 becomes d as in
     * \247d which is light purple)
     */
    public final Formatting rarityColor;

    /** Rarity name. */
    public final String rarityName;

    EnumRarity(Formatting color, String name)
    {
        rarityColor = color;
        rarityName = name;
    }
}
