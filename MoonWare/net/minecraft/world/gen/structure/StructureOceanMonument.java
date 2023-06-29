package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.init.Biomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class StructureOceanMonument extends MapGenStructure
{
    private int spacing;
    private int separation;
    public static final List<Biome> WATER_BIOMES = Arrays.asList(Biomes.OCEAN, Biomes.DEEP_OCEAN, Biomes.RIVER, Biomes.FROZEN_OCEAN, Biomes.FROZEN_RIVER);
    public static final List<Biome> SPAWN_BIOMES = Arrays.asList(Biomes.DEEP_OCEAN);
    private static final List<Biome.SpawnListEntry> MONUMENT_ENEMIES = Lists.newArrayList();

    public StructureOceanMonument()
    {
        spacing = 32;
        separation = 5;
    }

    public StructureOceanMonument(Map<String, String> p_i45608_1_)
    {
        this();

        for (Map.Entry<String, String> entry : p_i45608_1_.entrySet())
        {
            if (entry.getKey().equals("spacing"))
            {
                spacing = MathHelper.getInt(entry.getValue(), spacing, 1);
            }
            else if (entry.getKey().equals("separation"))
            {
                separation = MathHelper.getInt(entry.getValue(), separation, 1);
            }
        }
    }

    public String getStructureName()
    {
        return "Monument";
    }

    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
    {
        int i = chunkX;
        int j = chunkZ;

        if (chunkX < 0)
        {
            chunkX -= spacing - 1;
        }

        if (chunkZ < 0)
        {
            chunkZ -= spacing - 1;
        }

        int k = chunkX / spacing;
        int l = chunkZ / spacing;
        Random random = worldObj.setRandomSeed(k, l, 10387313);
        k = k * spacing;
        l = l * spacing;
        k = k + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;
        l = l + (random.nextInt(spacing - separation) + random.nextInt(spacing - separation)) / 2;

        if (i == k && j == l)
        {
            if (!worldObj.getBiomeProvider().areBiomesViable(i * 16 + 8, j * 16 + 8, 16, SPAWN_BIOMES))
            {
                return false;
            }

            boolean flag = worldObj.getBiomeProvider().areBiomesViable(i * 16 + 8, j * 16 + 8, 29, WATER_BIOMES);

            return flag;
        }

        return false;
    }

    public BlockPos getClosestStrongholdPos(World worldIn, BlockPos pos, boolean p_180706_3_)
    {
        worldObj = worldIn;
        return MapGenStructure.func_191069_a(worldIn, this, pos, spacing, separation, 10387313, true, 100, p_180706_3_);
    }

    protected StructureStart getStructureStart(int chunkX, int chunkZ)
    {
        return new StructureOceanMonument.StartMonument(worldObj, rand, chunkX, chunkZ);
    }

    public List<Biome.SpawnListEntry> getScatteredFeatureSpawnList()
    {
        return MONUMENT_ENEMIES;
    }

    static
    {
        MONUMENT_ENEMIES.add(new Biome.SpawnListEntry(EntityGuardian.class, 1, 2, 4));
    }

    public static class StartMonument extends StructureStart
    {
        private final Set<ChunkPos> processed = Sets.newHashSet();
        private boolean wasCreated;

        public StartMonument()
        {
        }

        public StartMonument(World worldIn, Random random, int chunkX, int chunkZ)
        {
            super(chunkX, chunkZ);
            create(worldIn, random, chunkX, chunkZ);
        }

        private void create(World worldIn, Random random, int chunkX, int chunkZ)
        {
            random.setSeed(worldIn.getSeed());
            long i = random.nextLong();
            long j = random.nextLong();
            long k = (long)chunkX * i;
            long l = (long)chunkZ * j;
            random.setSeed(k ^ l ^ worldIn.getSeed());
            int i1 = chunkX * 16 + 8 - 29;
            int j1 = chunkZ * 16 + 8 - 29;
            EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(random);
            components.add(new StructureOceanMonumentPieces.MonumentBuilding(random, i1, j1, enumfacing));
            updateBoundingBox();
            wasCreated = true;
        }

        public void generateStructure(World worldIn, Random rand, StructureBoundingBox structurebb)
        {
            if (!wasCreated)
            {
                components.clear();
                create(worldIn, rand, getChunkPosX(), getChunkPosZ());
            }

            super.generateStructure(worldIn, rand, structurebb);
        }

        public boolean isValidForPostProcess(ChunkPos pair)
        {
            return !processed.contains(pair) && super.isValidForPostProcess(pair);
        }

        public void notifyPostProcessAt(ChunkPos pair)
        {
            super.notifyPostProcessAt(pair);
            processed.add(pair);
        }

        public void writeToNBT(NBTTagCompound tagCompound)
        {
            super.writeToNBT(tagCompound);
            NBTTagList nbttaglist = new NBTTagList();

            for (ChunkPos chunkpos : processed)
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setInteger("X", chunkpos.chunkXPos);
                nbttagcompound.setInteger("Z", chunkpos.chunkZPos);
                nbttaglist.appendTag(nbttagcompound);
            }

            tagCompound.setTag("Processed", nbttaglist);
        }

        public void readFromNBT(NBTTagCompound tagCompound)
        {
            super.readFromNBT(tagCompound);

            if (tagCompound.hasKey("Processed", 9))
            {
                NBTTagList nbttaglist = tagCompound.getTagList("Processed", 10);

                for (int i = 0; i < nbttaglist.tagCount(); ++i)
                {
                    NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                    processed.add(new ChunkPos(nbttagcompound.getInteger("X"), nbttagcompound.getInteger("Z")));
                }
            }
        }
    }
}
