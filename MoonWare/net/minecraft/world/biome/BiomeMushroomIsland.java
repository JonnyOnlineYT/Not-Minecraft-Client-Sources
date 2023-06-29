package net.minecraft.world.biome;

import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.init.Blocks;

public class BiomeMushroomIsland extends Biome
{
    public BiomeMushroomIsland(Biome.BiomeProperties properties)
    {
        super(properties);
        theBiomeDecorator.treesPerChunk = -100;
        theBiomeDecorator.flowersPerChunk = -100;
        theBiomeDecorator.grassPerChunk = -100;
        theBiomeDecorator.mushroomsPerChunk = 1;
        theBiomeDecorator.bigMushroomsPerChunk = 1;
        topBlock = Blocks.MYCELIUM.getDefaultState();
        spawnableMonsterList.clear();
        spawnableCreatureList.clear();
        spawnableWaterCreatureList.clear();
        spawnableCreatureList.add(new Biome.SpawnListEntry(EntityMooshroom.class, 8, 4, 8));
    }
}
