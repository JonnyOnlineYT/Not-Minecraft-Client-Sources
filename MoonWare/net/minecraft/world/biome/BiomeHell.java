package net.minecraft.world.biome;

import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;

public class BiomeHell extends Biome
{
    public BiomeHell(Biome.BiomeProperties properties)
    {
        super(properties);
        spawnableMonsterList.clear();
        spawnableCreatureList.clear();
        spawnableWaterCreatureList.clear();
        spawnableCaveCreatureList.clear();
        spawnableMonsterList.add(new Biome.SpawnListEntry(EntityGhast.class, 50, 4, 4));
        spawnableMonsterList.add(new Biome.SpawnListEntry(EntityPigZombie.class, 100, 4, 4));
        spawnableMonsterList.add(new Biome.SpawnListEntry(EntityMagmaCube.class, 2, 4, 4));
        spawnableMonsterList.add(new Biome.SpawnListEntry(EntityEnderman.class, 1, 4, 4));
        theBiomeDecorator = new BiomeHellDecorator();
    }
}
