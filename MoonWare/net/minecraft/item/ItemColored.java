package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemColored extends ItemBlock
{
    private String[] subtypeNames;

    public ItemColored(Block block, boolean hasSubtypes)
    {
        super(block);

        if (hasSubtypes)
        {
            setMaxDamage(0);
            setHasSubtypes(true);
        }
    }

    /**
     * Converts the given ItemStack damage value into a metadata value to be placed in the world when this Item is
     * placed as a Block (mostly used with ItemBlocks).
     */
    public int getMetadata(int damage)
    {
        return damage;
    }

    public ItemColored setSubtypeNames(String[] names)
    {
        subtypeNames = names;
        return this;
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack stack)
    {
        if (subtypeNames == null)
        {
            return super.getUnlocalizedName(stack);
        }
        else
        {
            int i = stack.getMetadata();
            return i >= 0 && i < subtypeNames.length ? super.getUnlocalizedName(stack) + "." + subtypeNames[i] : super.getUnlocalizedName(stack);
        }
    }
}
