package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class MapGenStronghold extends MapGenStructure
{
    private final List<Biome> allowedBiomes;

    /**
     * is spawned false and set true once the defined BiomeGenBases were compared with the present ones
     */
    private boolean ranBiomeCheck;
    private ChunkPos[] structureCoords;
    private double distance;
    private int spread;

    public MapGenStronghold()
    {
        structureCoords = new ChunkPos[128];
        distance = 32.0D;
        spread = 3;
        allowedBiomes = Lists.newArrayList();

        for (Biome biome : Biome.REGISTRY)
        {
            if (biome != null && biome.getBaseHeight() > 0.0F)
            {
                allowedBiomes.add(biome);
            }
        }
    }

    public MapGenStronghold(Map<String, String> p_i2068_1_)
    {
        this();

        for (Map.Entry<String, String> entry : p_i2068_1_.entrySet())
        {
            if (entry.getKey().equals("distance"))
            {
                distance = MathHelper.getDouble(entry.getValue(), distance, 1.0D);
            }
            else if (entry.getKey().equals("count"))
            {
                structureCoords = new ChunkPos[MathHelper.getInt(entry.getValue(), structureCoords.length, 1)];
            }
            else if (entry.getKey().equals("spread"))
            {
                spread = MathHelper.getInt(entry.getValue(), spread, 1);
            }
        }
    }

    public String getStructureName()
    {
        return "Stronghold";
    }

    public BlockPos getClosestStrongholdPos(World worldIn, BlockPos pos, boolean p_180706_3_)
    {
        if (!ranBiomeCheck)
        {
            generatePositions();
            ranBiomeCheck = true;
        }

        BlockPos blockpos = null;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(0, 0, 0);
        double d0 = Double.MAX_VALUE;

        for (ChunkPos chunkpos : structureCoords)
        {
            blockpos$mutableblockpos.setPos((chunkpos.chunkXPos << 4) + 8, 32, (chunkpos.chunkZPos << 4) + 8);
            double d1 = blockpos$mutableblockpos.distanceSq(pos);

            if (blockpos == null)
            {
                blockpos = new BlockPos(blockpos$mutableblockpos);
                d0 = d1;
            }
            else if (d1 < d0)
            {
                blockpos = new BlockPos(blockpos$mutableblockpos);
                d0 = d1;
            }
        }

        return blockpos;
    }

    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
    {
        if (!ranBiomeCheck)
        {
            generatePositions();
            ranBiomeCheck = true;
        }

        for (ChunkPos chunkpos : structureCoords)
        {
            if (chunkX == chunkpos.chunkXPos && chunkZ == chunkpos.chunkZPos)
            {
                return true;
            }
        }

        return false;
    }

    private void generatePositions()
    {
        initializeStructureData(worldObj);
        int i = 0;
        ObjectIterator lvt_2_1_ = structureMap.values().iterator();

        while (lvt_2_1_.hasNext())
        {
            StructureStart structurestart = (StructureStart)lvt_2_1_.next();

            if (i < structureCoords.length)
            {
                structureCoords[i++] = new ChunkPos(structurestart.getChunkPosX(), structurestart.getChunkPosZ());
            }
        }

        Random random = new Random();
        random.setSeed(worldObj.getSeed());
        double d1 = random.nextDouble() * Math.PI * 2.0D;
        int j = 0;
        int k = 0;
        int l = structureMap.size();

        if (l < structureCoords.length)
        {
            for (int i1 = 0; i1 < structureCoords.length; ++i1)
            {
                double d0 = 4.0D * distance + distance * (double)j * 6.0D + (random.nextDouble() - 0.5D) * distance * 2.5D;
                int j1 = (int)Math.round(Math.cos(d1) * d0);
                int k1 = (int)Math.round(Math.sin(d1) * d0);
                BlockPos blockpos = worldObj.getBiomeProvider().findBiomePosition((j1 << 4) + 8, (k1 << 4) + 8, 112, allowedBiomes, random);

                if (blockpos != null)
                {
                    j1 = blockpos.getX() >> 4;
                    k1 = blockpos.getZ() >> 4;
                }

                if (i1 >= l)
                {
                    structureCoords[i1] = new ChunkPos(j1, k1);
                }

                d1 += (Math.PI * 2D) / (double) spread;
                ++k;

                if (k == spread)
                {
                    ++j;
                    k = 0;
                    spread += 2 * spread / (j + 1);
                    spread = Math.min(spread, structureCoords.length - i1);
                    d1 += random.nextDouble() * Math.PI * 2.0D;
                }
            }
        }
    }

    protected StructureStart getStructureStart(int chunkX, int chunkZ)
    {
        MapGenStronghold.Start mapgenstronghold$start;

        for (mapgenstronghold$start = new MapGenStronghold.Start(worldObj, rand, chunkX, chunkZ); mapgenstronghold$start.getComponents().isEmpty() || ((StructureStrongholdPieces.Stairs2)mapgenstronghold$start.getComponents().get(0)).strongholdPortalRoom == null; mapgenstronghold$start = new MapGenStronghold.Start(worldObj, rand, chunkX, chunkZ))
        {
        }

        return mapgenstronghold$start;
    }

    public static class Start extends StructureStart
    {
        public Start()
        {
        }

        public Start(World worldIn, Random random, int chunkX, int chunkZ)
        {
            super(chunkX, chunkZ);
            StructureStrongholdPieces.prepareStructurePieces();
            StructureStrongholdPieces.Stairs2 structurestrongholdpieces$stairs2 = new StructureStrongholdPieces.Stairs2(0, random, (chunkX << 4) + 2, (chunkZ << 4) + 2);
            components.add(structurestrongholdpieces$stairs2);
            structurestrongholdpieces$stairs2.buildComponent(structurestrongholdpieces$stairs2, components, random);
            List<StructureComponent> list = structurestrongholdpieces$stairs2.pendingChildren;

            while (!list.isEmpty())
            {
                int i = random.nextInt(list.size());
                StructureComponent structurecomponent = list.remove(i);
                structurecomponent.buildComponent(structurestrongholdpieces$stairs2, components, random);
            }

            updateBoundingBox();
            markAvailableHeight(worldIn, random, 10);
        }
    }
}
