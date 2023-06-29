package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockBone extends BlockRotatedPillar
{
    public BlockBone()
    {
        super(Material.ROCK, MapColor.SAND);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        setHardness(2.0F);
        setSoundType(SoundType.STONE);
    }
}
