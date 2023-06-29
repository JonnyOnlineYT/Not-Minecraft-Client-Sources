package net.minecraft.world.gen.structure;

import net.minecraft.init.Biomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MapGenVillage extends MapGenStructure
{
    public static final List<Biome> VILLAGE_SPAWN_BIOMES = Arrays.asList(Biomes.PLAINS, Biomes.DESERT, Biomes.SAVANNA, Biomes.TAIGA);

    /** None */
    private int size;
    private int distance;
    private final int minTownSeparation;

    public MapGenVillage()
    {
        distance = 32;
        minTownSeparation = 8;
    }

    public MapGenVillage(Map<String, String> map)
    {
        this();

        for (Map.Entry<String, String> entry : map.entrySet())
        {
            if (entry.getKey().equals("size"))
            {
                size = MathHelper.getInt(entry.getValue(), size, 0);
            }
            else if (entry.getKey().equals("distance"))
            {
                distance = MathHelper.getInt(entry.getValue(), distance, 9);
            }
        }
    }

    public String getStructureName()
    {
        return "Village";
    }

    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
    {
        int i = chunkX;
        int j = chunkZ;

        if (chunkX < 0)
        {
            chunkX -= distance - 1;
        }

        if (chunkZ < 0)
        {
            chunkZ -= distance - 1;
        }

        int k = chunkX / distance;
        int l = chunkZ / distance;
        Random random = worldObj.setRandomSeed(k, l, 10387312);
        k = k * distance;
        l = l * distance;
        k = k + random.nextInt(distance - 8);
        l = l + random.nextInt(distance - 8);

        if (i == k && j == l)
        {
            boolean flag = worldObj.getBiomeProvider().areBiomesViable(i * 16 + 8, j * 16 + 8, 0, VILLAGE_SPAWN_BIOMES);

            return flag;
        }

        return false;
    }

    public BlockPos getClosestStrongholdPos(World worldIn, BlockPos pos, boolean p_180706_3_)
    {
        worldObj = worldIn;
        return MapGenStructure.func_191069_a(worldIn, this, pos, distance, 8, 10387312, false, 100, p_180706_3_);
    }

    protected StructureStart getStructureStart(int chunkX, int chunkZ)
    {
        return new MapGenVillage.Start(worldObj, rand, chunkX, chunkZ, size);
    }

    public static class Start extends StructureStart
    {
        private boolean hasMoreThanTwoComponents;

        public Start()
        {
        }

        public Start(World worldIn, Random rand, int x, int z, int size)
        {
            super(x, z);
            List<StructureVillagePieces.PieceWeight> list = StructureVillagePieces.getStructureVillageWeightedPieceList(rand, size);
            StructureVillagePieces.Start structurevillagepieces$start = new StructureVillagePieces.Start(worldIn.getBiomeProvider(), 0, rand, (x << 4) + 2, (z << 4) + 2, list, size);
            components.add(structurevillagepieces$start);
            structurevillagepieces$start.buildComponent(structurevillagepieces$start, components, rand);
            List<StructureComponent> list1 = structurevillagepieces$start.pendingRoads;
            List<StructureComponent> list2 = structurevillagepieces$start.pendingHouses;

            while (!list1.isEmpty() || !list2.isEmpty())
            {
                if (list1.isEmpty())
                {
                    int i = rand.nextInt(list2.size());
                    StructureComponent structurecomponent = list2.remove(i);
                    structurecomponent.buildComponent(structurevillagepieces$start, components, rand);
                }
                else
                {
                    int j = rand.nextInt(list1.size());
                    StructureComponent structurecomponent2 = list1.remove(j);
                    structurecomponent2.buildComponent(structurevillagepieces$start, components, rand);
                }
            }

            updateBoundingBox();
            int k = 0;

            for (StructureComponent structurecomponent1 : components)
            {
                if (!(structurecomponent1 instanceof StructureVillagePieces.Road))
                {
                    ++k;
                }
            }

            hasMoreThanTwoComponents = k > 2;
        }

        public boolean isSizeableStructure()
        {
            return hasMoreThanTwoComponents;
        }

        public void writeToNBT(NBTTagCompound tagCompound)
        {
            super.writeToNBT(tagCompound);
            tagCompound.setBoolean("Valid", hasMoreThanTwoComponents);
        }

        public void readFromNBT(NBTTagCompound tagCompound)
        {
            super.readFromNBT(tagCompound);
            hasMoreThanTwoComponents = tagCompound.getBoolean("Valid");
        }
    }
}
