package net.minecraft.tileentity;

import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;

public class TileEntityDropper extends TileEntityDispenser
{
    public static void registerFixesDropper(DataFixer fixer)
    {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityDropper.class, "Items"));
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName()
    {
        return hasCustomName() ? field_190577_o : "container.dropper";
    }

    public String getGuiID()
    {
        return "minecraft:dropper";
    }
}
