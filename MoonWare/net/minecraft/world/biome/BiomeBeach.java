package net.minecraft.world.biome;

import net.minecraft.init.Blocks;

public class BiomeBeach extends Biome
{
    public BiomeBeach(Biome.BiomeProperties properties)
    {
        super(properties);
        spawnableCreatureList.clear();
        topBlock = Blocks.SAND.getDefaultState();
        fillerBlock = Blocks.SAND.getDefaultState();
        theBiomeDecorator.treesPerChunk = -999;
        theBiomeDecorator.deadBushPerChunk = 0;
        theBiomeDecorator.reedsPerChunk = 0;
        theBiomeDecorator.cactiPerChunk = 0;
    }
}
