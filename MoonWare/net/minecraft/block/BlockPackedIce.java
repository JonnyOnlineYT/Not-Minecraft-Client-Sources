package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

import java.util.Random;

public class BlockPackedIce extends Block
{
    public BlockPackedIce()
    {
        super(Material.PACKED_ICE);
        slipperiness = 0.98F;
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random)
    {
        return 0;
    }
}
