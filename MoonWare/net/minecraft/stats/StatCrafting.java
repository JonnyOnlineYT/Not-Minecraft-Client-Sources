package net.minecraft.stats;

import net.minecraft.item.Item;
import net.minecraft.util.text.Component;

public class StatCrafting extends StatBase
{
    private final Item item;

    public StatCrafting(String p_i45910_1_, String p_i45910_2_, Component statNameIn, Item p_i45910_4_)
    {
        super(p_i45910_1_ + p_i45910_2_, statNameIn);
        item = p_i45910_4_;
    }

    public Item getItem()
    {
        return item;
    }
}
