package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class MapGenScatteredFeature extends MapGenStructure
{
    private static final List<Biome> BIOMELIST = Arrays.asList(Biomes.DESERT, Biomes.DESERT_HILLS, Biomes.JUNGLE, Biomes.JUNGLE_HILLS, Biomes.SWAMPLAND, Biomes.ICE_PLAINS, Biomes.COLD_TAIGA);
    private final List<Biome.SpawnListEntry> scatteredFeatureSpawnList;

    /** the maximum distance between scattered features */
    private int maxDistanceBetweenScatteredFeatures;

    /** the minimum distance between scattered features */
    private final int minDistanceBetweenScatteredFeatures;

    public MapGenScatteredFeature()
    {
        scatteredFeatureSpawnList = Lists.newArrayList();
        maxDistanceBetweenScatteredFeatures = 32;
        minDistanceBetweenScatteredFeatures = 8;
        scatteredFeatureSpawnList.add(new Biome.SpawnListEntry(EntityWitch.class, 1, 1, 1));
    }

    public MapGenScatteredFeature(Map<String, String> p_i2061_1_)
    {
        this();

        for (Map.Entry<String, String> entry : p_i2061_1_.entrySet())
        {
            if (entry.getKey().equals("distance"))
            {
                maxDistanceBetweenScatteredFeatures = MathHelper.getInt(entry.getValue(), maxDistanceBetweenScatteredFeatures, 9);
            }
        }
    }

    public String getStructureName()
    {
        return "Temple";
    }

    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
    {
        int i = chunkX;
        int j = chunkZ;

        if (chunkX < 0)
        {
            chunkX -= maxDistanceBetweenScatteredFeatures - 1;
        }

        if (chunkZ < 0)
        {
            chunkZ -= maxDistanceBetweenScatteredFeatures - 1;
        }

        int k = chunkX / maxDistanceBetweenScatteredFeatures;
        int l = chunkZ / maxDistanceBetweenScatteredFeatures;
        Random random = worldObj.setRandomSeed(k, l, 14357617);
        k = k * maxDistanceBetweenScatteredFeatures;
        l = l * maxDistanceBetweenScatteredFeatures;
        k = k + random.nextInt(maxDistanceBetweenScatteredFeatures - 8);
        l = l + random.nextInt(maxDistanceBetweenScatteredFeatures - 8);

        if (i == k && j == l)
        {
            Biome biome = worldObj.getBiomeProvider().getBiome(new BlockPos(i * 16 + 8, 0, j * 16 + 8));

            if (biome == null)
            {
                return false;
            }

            for (Biome biome1 : BIOMELIST)
            {
                if (biome == biome1)
                {
                    return true;
                }
            }
        }

        return false;
    }

    public BlockPos getClosestStrongholdPos(World worldIn, BlockPos pos, boolean p_180706_3_)
    {
        worldObj = worldIn;
        return MapGenStructure.func_191069_a(worldIn, this, pos, maxDistanceBetweenScatteredFeatures, 8, 14357617, false, 100, p_180706_3_);
    }

    protected StructureStart getStructureStart(int chunkX, int chunkZ)
    {
        return new MapGenScatteredFeature.Start(worldObj, rand, chunkX, chunkZ);
    }

    public boolean isSwampHut(BlockPos p_175798_1_)
    {
        StructureStart structurestart = getStructureAt(p_175798_1_);

        if (structurestart != null && structurestart instanceof MapGenScatteredFeature.Start && !structurestart.components.isEmpty())
        {
            StructureComponent structurecomponent = structurestart.components.get(0);
            return structurecomponent instanceof ComponentScatteredFeaturePieces.SwampHut;
        }
        else
        {
            return false;
        }
    }

    public List<Biome.SpawnListEntry> getScatteredFeatureSpawnList()
    {
        return scatteredFeatureSpawnList;
    }

    public static class Start extends StructureStart
    {
        public Start()
        {
        }

        public Start(World worldIn, Random random, int chunkX, int chunkZ)
        {
            this(worldIn, random, chunkX, chunkZ, worldIn.getBiome(new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8)));
        }

        public Start(World worldIn, Random random, int chunkX, int chunkZ, Biome biomeIn)
        {
            super(chunkX, chunkZ);

            if (biomeIn != Biomes.JUNGLE && biomeIn != Biomes.JUNGLE_HILLS)
            {
                if (biomeIn == Biomes.SWAMPLAND)
                {
                    ComponentScatteredFeaturePieces.SwampHut componentscatteredfeaturepieces$swamphut = new ComponentScatteredFeaturePieces.SwampHut(random, chunkX * 16, chunkZ * 16);
                    components.add(componentscatteredfeaturepieces$swamphut);
                }
                else if (biomeIn != Biomes.DESERT && biomeIn != Biomes.DESERT_HILLS)
                {
                    if (biomeIn == Biomes.ICE_PLAINS || biomeIn == Biomes.COLD_TAIGA)
                    {
                        ComponentScatteredFeaturePieces.Igloo componentscatteredfeaturepieces$igloo = new ComponentScatteredFeaturePieces.Igloo(random, chunkX * 16, chunkZ * 16);
                        components.add(componentscatteredfeaturepieces$igloo);
                    }
                }
                else
                {
                    ComponentScatteredFeaturePieces.DesertPyramid componentscatteredfeaturepieces$desertpyramid = new ComponentScatteredFeaturePieces.DesertPyramid(random, chunkX * 16, chunkZ * 16);
                    components.add(componentscatteredfeaturepieces$desertpyramid);
                }
            }
            else
            {
                ComponentScatteredFeaturePieces.JunglePyramid componentscatteredfeaturepieces$junglepyramid = new ComponentScatteredFeaturePieces.JunglePyramid(random, chunkX * 16, chunkZ * 16);
                components.add(componentscatteredfeaturepieces$junglepyramid);
            }

            updateBoundingBox();
        }
    }
}
