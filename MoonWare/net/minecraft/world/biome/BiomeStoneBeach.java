package net.minecraft.world.biome;

import net.minecraft.init.Blocks;

public class BiomeStoneBeach extends Biome
{
    public BiomeStoneBeach(Biome.BiomeProperties properties)
    {
        super(properties);
        spawnableCreatureList.clear();
        topBlock = Blocks.STONE.getDefaultState();
        fillerBlock = Blocks.STONE.getDefaultState();
        theBiomeDecorator.treesPerChunk = -999;
        theBiomeDecorator.deadBushPerChunk = 0;
        theBiomeDecorator.reedsPerChunk = 0;
        theBiomeDecorator.cactiPerChunk = 0;
    }
}
