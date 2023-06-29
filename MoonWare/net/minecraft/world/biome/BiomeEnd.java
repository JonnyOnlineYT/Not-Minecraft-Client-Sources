package net.minecraft.world.biome;

import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.Blocks;

public class BiomeEnd extends Biome
{
    public BiomeEnd(Biome.BiomeProperties properties)
    {
        super(properties);
        spawnableMonsterList.clear();
        spawnableCreatureList.clear();
        spawnableWaterCreatureList.clear();
        spawnableCaveCreatureList.clear();
        spawnableMonsterList.add(new Biome.SpawnListEntry(EntityEnderman.class, 10, 4, 4));
        topBlock = Blocks.DIRT.getDefaultState();
        fillerBlock = Blocks.DIRT.getDefaultState();
        theBiomeDecorator = new BiomeEndDecorator();
    }

    /**
     * takes temperature, returns color
     */
    public int getSkyColorByTemp(float currentTemperature)
    {
        return 0;
    }
}
